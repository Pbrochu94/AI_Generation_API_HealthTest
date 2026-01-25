package tests

import io.restassured.RestAssured
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.*
import utils.MockServer
import utils.RequestBuilder


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ProjectApi {
    @BeforeAll
    fun startServer(){
        MockServer.start()
    }
    @Test
    fun `Login with valid credentials returns 200`(){

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
        val response = RequestBuilder.login("Artorias@hotmail.com", "123456")
            .`when`()
            .post("/login")
        .then()
        .statusCode(200)
        .extract()
        .response()
        println(response.body().jsonPath().getString(""))
    }
}