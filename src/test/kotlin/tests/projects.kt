package tests

import com.github.tomakehurst.wiremock.http.Response.response
import io.restassured.RestAssured
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.assertEquals
import utils.BaseTest
import utils.MockServer
import utils.Privacy
import utils.Project
import utils.ProjectData.projectIdInvalid
import utils.ProjectData.projectIdValid
import utils.ProjectData.projectValidBody
import utils.RequestBuilder
import utils.RequestBuilder.projectCreationPost
import utils.RequestBuilder.projectGet
import utils.getResponseError

import java.util.Date


class ProjectApi: BaseTest() {
    @Test
    fun `POST call with valid body to project endpoint return 201` (){
        val response = projectCreationPost(projectValidBody.random())
            .then()
            .log().all()
            .extract()
            .response()
        assertEquals(201, response.statusCode(), getResponseError(response))
    }
    @Test
    fun `POST call with invalid body to project endpoint return 400` (){
        val response = projectCreationPost(mapOf<String,String>("projectName" to "Eclipse"))
            .then()
            .log().all()
            .extract()
            .response()
        assertEquals(400, response.statusCode(), getResponseError(response))
    }
    @Test
    fun `GET call with valid project id return 200`(){
        val response = projectGet(projectIdValid.random())
            .then()
            .log().all()
        .extract()
        .response()
        assertEquals(200, response.statusCode(), getResponseError(response))
    }
    @Test
    fun `GET call with invalid project id return 404`(){
        val response = projectGet(projectIdInvalid.random())
            .then()
            .log().all()
            .extract()
            .response()
        assertEquals(404, response.statusCode(), getResponseError(response))
    }
}