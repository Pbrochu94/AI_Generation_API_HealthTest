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
        val newProject = Project("Abyss", Privacy.PRIVATE.string)
        MockServer.generateProjectMetaData(newProject)
        MockServer.addProjectToServer(newProject)
        val response = RestAssured.given()
            .baseUri(MockServer.baseUrl)
            //.log().body()
            .`when`()
            .body(newProject.getBaseInfoAsMap())
            .post("/project")
            .then()
            //.log().body()
        .statusCode(201)
        .extract()
            .response()

        println("--------------")
        println(MockServer.projects)
    }
    @Test
    fun `Get an existing project returns details`(){

    }
    @AfterAll
    fun destroyServer(){
        MockServer.stop()
    }
}