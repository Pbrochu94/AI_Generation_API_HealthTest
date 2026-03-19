package tests

import com.github.tomakehurst.wiremock.http.Response.response
import io.restassured.RestAssured
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.assertEquals
import utils.MockServer
import utils.Privacy
import utils.Project
import utils.ProjectData.invalidProjectId
import utils.ProjectData.projectValidBody
import utils.RequestBuilder
import utils.RequestBuilder.getProjectBuilder
import utils.RequestBuilder.projectCreationBuilder
import utils.getResponseError

import java.util.Date


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ProjectApi {
    @BeforeAll
    fun initServer(){
        MockServer.start()
    }
    @Test
    fun `POST call with valid body to project endpoint return 201` (){
        val response = projectCreationBuilder(projectValidBody.random())
            .log().body()
            .post("/project")
            .then()
            .log().body()
            .extract()
            .response()
        assertEquals(201, response.statusCode(), getResponseError(response))
    }
    @Test
    fun `POST call with invalid body to project endpoint return 401` (){
        val response = projectCreationBuilder(mapOf<String,String>("ProjectName" to "Virox"))
            .body(mapOf<String,String>("projectName" to "Eclipse"))
            .post("/project")
            .then()
            .log().body()
            .extract()
            .response()
        assertEquals(400, response.statusCode(), getResponseError(response))
    }
    @Test
    fun `GET call with valid project id return 200`(){
        val response = getProjectBuilder()
            .get("/project/${MockServer.projects.random().id}")
            .then()
            .log().body()
        .extract()
        .response()
        assertEquals(200, response.statusCode(), getResponseError(response))
    }
    @Test
    fun `GET call with invalid project id return 404`(){
        val response = getProjectBuilder()
            .get("/project/${invalidProjectId.random()}")
            .then()
            .log().body()
            .extract()
            .response()
        assertEquals(404, response.statusCode(), getResponseError(response))
    }
    @AfterAll
    fun destroyServer(){
        MockServer.stop()
    }
}