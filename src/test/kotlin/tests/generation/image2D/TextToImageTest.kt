package tests.generation.image2D

import io.restassured.response.Response
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import utils.models.BaseTest
import utils.data.ProjectData.projectIdValid
import utils.data.Prompt
import utils.enums.Provider
import utils.enums.Tool
import utils.helpers.RequestBuilder.postStep
import utils.models.Generation
import utils.server.MockServer.startJob


class GenV2: BaseTest() {
    @Test
    fun `test`(){
        val generation = Generation(Provider.GENV2.string,Tool.PROMPT_TO_IMAGE.string,Prompt.validPrompt.random())
        startJob(generation,"failed")
    }
    @Test
    fun `POST returns 201`(){
        val body:Map<String,Any> = mapOf(
            "provider" to Provider.GENV2.string,
            "tool" to Tool.PROMPT_TO_IMAGE.string,
            "prompt" to Prompt.validPrompt.random(),
        )
        val response: Response = postStep(projectIdValid.random(),body)
            .then()
            .log().all()
            .extract()
            .response()
        assertEquals(201, response.statusCode)
    }
    fun `POST returns in progress status`(){
        val body:Map<String,Any> = mapOf(
            "provider" to Provider.GENV2.string,
            "tool" to Tool.PROMPT_TO_IMAGE.string,
            "prompt" to Prompt.validPrompt.random(),
        )
        val response: Response = postStep(projectIdValid.random(),body)
            .then()
            .log().all()
            .extract()
            .response()
        assertEquals(201, response.statusCode)
    }
}