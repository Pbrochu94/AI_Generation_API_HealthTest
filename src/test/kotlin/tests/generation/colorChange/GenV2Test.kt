package tests.generation.colorChange

import io.restassured.response.Response
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
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


class GenV2ColorChangeTests: BaseTest() {
    private lateinit var project: Project
    private lateinit var generation: Generation
    @BeforeEach
    fun initTestParameters(){
        project = ProjectData.projects.random()
        generation = project.steps.first{it.provider == Providers.GENV2.string && it.tool == Tools.COLOR_CHANGE.string}
    }
    @Test
    fun `Successful generation returns 200 and generation result`(){
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
    @Test
    fun `Failed generation returns 200 and 'failed' status`(){
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