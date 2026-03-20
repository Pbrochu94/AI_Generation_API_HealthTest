package tests.step

import io.restassured.response.Response
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import utils.data.ProjectData.projectIdInvalid
import utils.data.ProjectData.projectIdValid
import utils.data.Prompt
import utils.enums.Provider
import utils.enums.Tool
import utils.helpers.RequestBuilder.postStep
import utils.helpers.getResponseError
import utils.models.BaseTest

class StepEndpoint:BaseTest() {
    @Test
    fun `POST call to step return 201`(){
        val body:Map<String,Any> = mapOf(
            "provider" to Provider.entries.random().string,
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
    @Test
    fun `POST call to step return with invalid project id returns 404`(){
        val body:Map<String,Any> = mapOf(
            "provider" to Provider.entries.random().string,
            "tool" to Tool.entries.random().string,
            "prompt" to Prompt.validPrompt.random(),
        )
        val response: Response = postStep(projectIdInvalid.random(),body)
            .then()
            .log().all()
            .extract()
            .response()
        assertEquals(404, response.statusCode , getResponseError(response))
    }
    @Test
    fun `POST call with missing provider returns 400`(){
        val body:Map<String,Any> = mapOf(
            "tool" to Tool.entries.random().string,
            "prompt" to Prompt.validPrompt.random(),
        )
        val response: Response = postStep(projectIdValid.random(),body)
            .then()
            .log().all()
            .extract()
            .response()
        assertEquals(400, response.statusCode , getResponseError(response))
    }
    @Test
    fun `POST call with invalid provider returns 400`(){
        val body:Map<String,Any> = mapOf(
            "provider" to "nonExistingProvider",
            "tool" to Tool.entries.random().string,
            "prompt" to Prompt.validPrompt.random(),
        )
        val response: Response = postStep(projectIdValid.random(),body)
            .then()
            .log().all()
            .extract()
            .response()
        assertEquals(400, response.statusCode , getResponseError(response))
    }
    @Test
    fun `POST call with missing tool returns 400`(){
        val body:Map<String,Any> = mapOf(
            "provider" to Provider.entries.random().string,
            "prompt" to Prompt.validPrompt.random(),
        )
        val response: Response = postStep(projectIdValid.random(),body)
            .then()
            .log().all()
            .extract()
            .response()
        assertEquals(400, response.statusCode , getResponseError(response))
    }
    @Test
    fun `POST call with invalid tool returns 400`(){
        val body:Map<String,Any> = mapOf(
            "provider" to Provider.entries.random().string,
            "tool" to "NonExistingTool",
            "prompt" to Prompt.validPrompt.random(),
        )
        val response: Response = postStep(projectIdValid.random(),body)
            .then()
            .log().all()
            .extract()
            .response()
        assertEquals(400, response.statusCode , getResponseError(response))
    }
    @Test
    fun `POST call with missing prompt returns 400`(){
        val body:Map<String,Any> = mapOf(
            "provider" to Provider.entries.random().string,
            "tool" to Tool.entries.random().string,
        )
        val response: Response = postStep(projectIdValid.random(),body)
            .then()
            .log().all()
            .extract()
            .response()
        assertEquals(400, response.statusCode , getResponseError(response))
    }
}