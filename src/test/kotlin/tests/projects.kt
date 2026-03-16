package tests

import io.restassured.RestAssured
import org.junit.jupiter.api.*
import utils.MockServer
import utils.RequestBuilder


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ProjectApi {
    @BeforeAll
    fun initServer(){
        MockServer.start()
    }
    @Test
    fun `project creation` (){
        val response = RestAssured.given()
            .baseUri(MockServer.baseUrl)
        .`when`()
        .post("/projects")
        .then()
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