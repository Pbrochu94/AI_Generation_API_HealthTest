package tests.generation.image2D

import io.restassured.response.Response
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import utils.data.GenerationData
import utils.data.ProjectData
import utils.models.BaseTest
import utils.data.ProjectData.projectIdValid
import utils.enums.Providers
import utils.helpers.RequestBuilder.getJob
import utils.helpers.RequestBuilder.postStep
import utils.models.Generation
import kotlin.test.assertNotNull


class GenV2: BaseTest() {
    @Test
    fun `test`(){
        GenerationData.jobList.forEach { job ->
            println(job)
        }
    }
    @Test
    fun `GET with valid job id returns 200 with status and progress parameters`(){
        GenerationData.jobList.forEach { job ->
            println(job.id)
        }
        val project = ProjectData.projects.random()
        val response: Response = getJob(project.id, project.steps?.random()?.id, Generation(provider = Providers.GENV2.string).getRequestBody())
            .then()
            .log().all()
            .extract()
            .response()

        assertAll(
            {
                assertEquals(200, response.statusCode)
                assertNotNull(response.body.jsonPath().getString("progress"))
            }
        )

    }
//    fun `POST returns in progress status`(){
//        val body:Map<String,Any> = mapOf(
//            "provider" to Provider.GENV2.string,
//            "tool" to Tool.PROMPT_TO_IMAGE.string,
//            "prompt" to Prompt.validPrompt.random(),
//        )
//        val response: Response = postStep(projectIdValid.random(),body)
//            .then()
//            .log().all()
//            .extract()
//            .response()
//        assertEquals(201, response.statusCode)
//    }
}