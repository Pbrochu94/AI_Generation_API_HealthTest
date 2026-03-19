package tests.image2D

import io.restassured.response.Response
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import utils.models.BaseTest
import utils.data.ProjectData.projectIdValid
import utils.helpers.RequestBuilder.postStep

class NewGen: BaseTest() {
    @Test
    fun `POST call to step return 201`(){
        println(projectIdValid)
        val body:Map<String,Any> = mapOf("provider" to "genv2")
        val response: Response = postStep(projectIdValid.random(),body)
            .then()
            .extract()
            .response()
        assertEquals(201, response.statusCode)
    }
}