package tests.generation.image2D

import io.restassured.response.Response
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import utils.data.GenerationData
import utils.models.BaseTest
import utils.data.ProjectData.projectIdValid
import utils.enums.Provider
import utils.helpers.RequestBuilder.postStep
import utils.models.Generation


class GenV2: BaseTest() {
    @Test
    fun `test`(){
        GenerationData.jobList.forEach { job ->
            println(job)
        }
    }
    @Test
    fun `GET with valid job id returns 200 with status and progress parameters`(){
        val response: Response = postStep(projectIdValid.random(), Generation(provider = Provider.GENV2.string).getRequestBody())
            .then()
            .log().all()
            .extract()
            .response()
        assertAll(
            {
                assertEquals(200, response.statusCode)
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