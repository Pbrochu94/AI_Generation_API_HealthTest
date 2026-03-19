package tests.image2D

import io.restassured.response.Response
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import utils.data.ProjectData.projectIdInvalid
import utils.models.BaseTest
import utils.data.ProjectData.projectIdValid
import utils.enums.Provider
import utils.enums.Tool
import utils.helpers.RequestBuilder.postStep
import utils.helpers.getResponseError

class NewGen: BaseTest() {
    @Test
    fun `POST call to step return 201`(){
        val body:Map<String,Any> = mapOf(
            "provider" to Provider.MAKO.string,
            "tool" to Tool.PROMPT_TO_IMAGE.string,
            "prompt" to "A dog",
        )
        val response: Response = postStep(projectIdValid.random(),body)
            .then()
            .log().all()
            .extract()
            .response()
        assertEquals(201, response.statusCode)
    }
    @Test
    fun `Test`(){
        println(Tool.toolsToRegex())
        val body:Map<String,Any> = mapOf(
            "provider" to "sdas",
            "tool" to "mmk",
            "prompt" to "badt",
        )
        val response: Response = postStep(projectIdValid.random(),body)
            .then()
            //.log().all()
            .extract()
            .response()
        assertEquals(201, response.statusCode , getResponseError(response))
    }
}