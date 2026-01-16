package tests

import io.restassured.RestAssured
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.*
import utils.MockServer
import utils.MockServer.baseUrl
import utils.MockServer.start
import utils.MockServer.stop
import utils.projectAPIData

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ProjectApi {
    @BeforeAll
    fun startServer(){
        MockServer.start()
    }
    @Test
    fun `project creation` (){
        val response = RestAssured.given()
            .baseUri(baseUrl)
        .`when`()
        .post("/projects")
        .then()
        .statusCode(200)
        .extract()
            .body().asString()
        println(response)
    }
}