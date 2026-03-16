package tests

import com.github.tomakehurst.wiremock.http.Response.response
import io.restassured.RestAssured
import org.junit.jupiter.api.*
import utils.MockServer
import utils.Privacy
import utils.Project
import utils.RequestBuilder


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ProjectApi {
    @BeforeAll
    fun initServer(){
        MockServer.start()
    }
    @Test
    fun `project creation` (){
        val newProject = Project(inputName = "Abyss", Privacy.PRIVATE)
        val response = RestAssured.given()
            .baseUri(MockServer.baseUrl)
            .log().body()
            .`when`()
            .body(mapOf("name" to newProject.getName(),
                "privacy" to newProject.getPrivacy())
            )
            .post("/project")
            .then()
            .log().body()
        .statusCode(201)
        .extract()
            .response()
        println(response.body().jsonPath().getString(""))
    }
    @Test
    fun `Get an existing project returns details`(){

    }
    @AfterAll
    fun destroyServer(){
        MockServer.stop()
    }
}