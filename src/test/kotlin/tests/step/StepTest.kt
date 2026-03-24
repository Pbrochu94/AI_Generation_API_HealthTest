package tests.step

import io.restassured.response.Response
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import utils.data.GenerationData
import utils.data.ProjectData.projectIdInvalid
import utils.data.ProjectData.projectIdValid
import utils.helpers.RequestBuilder.postStep
import utils.helpers.getResponseError
import utils.models.BaseTest
import utils.models.Generation

class StepEndpoint:BaseTest() {
    @Test
    fun `POST call to step return 201`(){
        val response: Response = postStep(projectIdValid.random(), GenerationData.jobList.random().getRequestBody())
            .then()
            .log().all()
            .extract()
            .response()
        assertEquals(201, response.statusCode)
    }
    @Test
    fun `POST call to step return with invalid project id returns 404`(){
        val response: Response = postStep(projectIdInvalid.random(), GenerationData.jobList.random().getRequestBody())
            .then()
            .log().all()
            .extract()
            .response()
        assertEquals(404, response.statusCode){response.asString()}
    }
    @Test
    fun `POST call with missing provider returns 400 and accurate error message`(){
        val generationBody = Generation().getRequestBody()
        generationBody.remove("provider")
        val response: Response = postStep(projectIdValid.random(),generationBody)
            .then()
            .log().all()
            .extract()
            .response()
        assertAll(
            {
                assertEquals(400, response.statusCode)
                assertEquals("Missing required 'provider' parameter in body", getResponseError(response))
            }
        )
    }
    @Test
    fun `POST call with invalid provider returns and accurate error message`(){
        val generationBody = Generation().getRequestBody()
        generationBody["provider"] = "Invalid provider"
        val response: Response = postStep(projectIdValid.random(),generationBody)
            .then()
            .log().all()
            .extract()
            .response()
        assertAll(
            {
                assertEquals(400, response.statusCode)
                assertEquals("Provider selected is invalid", getResponseError(response))
            }
        )
    }
    @Test
    fun `POST call with missing tool returns 400 and accurate message`(){
        val generationBody = Generation().getRequestBody()
        generationBody.remove("tool")
        val response: Response = postStep(projectIdValid.random(),generationBody)
            .then()
            .log().all()
            .extract()
            .response()
        assertAll(
            {
                assertEquals(400, response.statusCode)
                assertEquals("Missing required 'tool' parameter in body", getResponseError(response))
            }
        )
    }
    @Test
    fun `POST call with invalid tool returns 400 with accurate error message`(){
        val generationBody = Generation().getRequestBody()
        generationBody["tool"] = "Invalid tool"
        val response: Response = postStep(projectIdValid.random(),generationBody)
            .then()
            .log().all()
            .extract()
            .response()
        assertAll(
            {
            assertEquals(400, response.statusCode)
            assertEquals("Tool selected is invalid", getResponseError(response))
            }
        )
    }
    @Test
    fun `POST call with missing prompt returns 400 and accurate error message`(){
        val generationBody = Generation().getRequestBody()
        generationBody.remove("prompt")
        val response: Response = postStep(projectIdValid.random(),generationBody)
            .then()
            .log().all()
            .extract()
            .response()
        assertAll(
            {
                assertEquals(400, response.statusCode)
                assertEquals("Missing required 'prompt' parameter in body", getResponseError(response))
            }
        )
    }
}