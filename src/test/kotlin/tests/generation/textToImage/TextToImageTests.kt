package tests.generation.textToImage

import io.qameta.allure.LabelAnnotation
import io.restassured.response.Response
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.assertAll
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.EnumSource
import utils.data.GenerationData
import utils.data.ProjectData
import utils.models.BaseTest
import utils.data.GenerationData.Providers
import utils.enums.Timer
import utils.helpers.RequestBuilder.getJob
import utils.helpers.pollGet
import utils.models.Generation
import utils.models.Project
import kotlin.test.assertNotNull
import kotlin.test.assertTrue
import utils.data.GenerationData.Status
import utils.data.GenerationData.Tools
import io.qameta.allure.Story


class TextToImage: BaseTest() {
    private lateinit var project: Project
    private lateinit var generation: Generation
    @BeforeEach
    fun initTestParameters(){
        project = ProjectData.projects.random()
    }
    @Story("Success case")
    @EnumSource(Providers::class)
    @ParameterizedTest(name = "Text to Image generation with {0} provider")
    fun `Successful generation returns 200 and generation result`(provider:Providers) {
        generation = project.steps.first{it.provider == provider.string && it.tool == Tools.PROMPT_TO_IMAGE.string}
        generation.launchAsyncGenerationSuccess()
        pollGet(project.id ,generation, Timer.TIMEOUT2D)
        val completedResponse:Response = getJob(project.id,generation.id, generation.getRequestBody())
            .then()
            .extract()
            .response()
        val outputFormat = completedResponse.body.jsonPath().getString("format")
        assertAll(
            {
                assertEquals(200, completedResponse.statusCode())
                assertEquals(Status.SUCCESS.string, completedResponse.body.jsonPath().getString("status"))
                assertNotNull(completedResponse.body.jsonPath().getString("imageUrl"))
                assertTrue(GenerationData.Image2DFormat.entries.map{it.string}.contains(outputFormat))
            }
        )
    }
    @Story("Failure case")
    @EnumSource(Providers::class)
    @ParameterizedTest(name = "Text to Image generation with {0} provider")
    fun `Failed generation returns 200 and 'failed' status`(provider:Providers) {
        generation = project.steps.first{it.provider == provider.string && it.tool == Tools.PROMPT_TO_IMAGE.string}
        generation.launchAsyncGenerationFailed()
        pollGet(project.id ,generation, Timer.TIMEOUT2D)
        val completedResponse:Response = getJob(project.id,generation.id, generation.getRequestBody())
            .then()
            .extract()
            .response()
        assertAll(
            {
                assertEquals(200, completedResponse.statusCode())
                assertEquals(Status.FAILURE.string, completedResponse.body.jsonPath().getString("status"))
                assertTrue(completedResponse.body.jsonPath().getString("imageUrl").isNullOrEmpty())
                assertTrue(completedResponse.body.jsonPath().getString("format").isNullOrEmpty())
            }
        )
    }
    @AfterEach
    fun destroyTestParameters(){
        generation.clear()
    }
}