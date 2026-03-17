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
    fun `POST call with valid body to project endpoint return 201` (){
        val response = RestAssured.given()
            .baseUri(MockServer.baseUrl)
            //.log().body()
            .`when`()
            .body(MockServer.projects.random())
            .post("/project")
            .then()
            .log().body()
        .statusCode(201)
    }
    @Test
    fun `POST call with invalid body to project endpoint return 400` (){}
    @Test
    fun `Get an existing project returns details`(){

    }
    @AfterAll
    fun destroyServer(){
        MockServer.stop()
    }
}