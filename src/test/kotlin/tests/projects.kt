package tests

import com.github.tomakehurst.wiremock.http.Response.response
import io.restassured.RestAssured
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.assertEquals
import utils.MockServer
import utils.Privacy
import utils.Project
import utils.RequestBuilder
import utils.RequestBuilder.projectBuilder
import utils.getResponseError
import utils.usersWithInvalidMail
import java.util.Date


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ProjectApi {
    @BeforeAll
    fun initServer(){
        MockServer.start()
    }
    @Test
    fun `POST call with valid body to project endpoint return 201` (){
        val response = projectBuilder(MockServer.projects.random().getParametersAsMap())
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
        val response = projectBuilder(mapOf<String,String>("ProjectName" to "Virox"))
            .body(mapOf<String,String>("projectName" to "Eclipse"))
            .post("/project")
            .then()
            .log().body()
            .extract()
            .response()
        assertEquals(401, response.statusCode(), getResponseError(response))
    }
    @Test
    fun `Get an existing project returns details`(){

    }
    @AfterAll
    fun destroyServer(){
        MockServer.stop()
    }
}