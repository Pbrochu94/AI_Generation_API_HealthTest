package utils.helpers

import io.restassured.RestAssured
import io.restassured.response.Response
import utils.enums.Environments
import utils.server.MockServer

object RequestBuilder{
    fun loginPost(user:Map<String,String> ): Response{
        val response: Response = RestAssured.given()
            .baseUri(Environments.BASE_URL.url)
            .header("Content-Type", "application/json")
            .header("Accept", "application/json")
            .body("""
                {
                "mail": "${user.getValue("mail")}",
                "password": "${user.getValue("password")}"
                }
            """.trimIndent())
            .`when`()
            .post("/login")
        return response
    }
    fun projectCreationPost(body:Map<String,Any?>): Response {
        val response: Response = RestAssured.given()
            .baseUri(Environments.BASE_URL.url)
            .header("Content-Type", "application/json")
            .header("Accept", "application/json")
            .body(body)
            .`when`()
            .post("/project")
        return response
    }
    fun projectGet(id:String?): Response{
        val response: Response = RestAssured.given()
            .baseUri(Environments.BASE_URL.url)
            .header("Content-Type", "application/json")
            .header("Accept", "application/json")
            .`when`()
            .get("/project/${id}")
        return response
    }
    fun postStep(projectId:String,body:Map<String,Any?>): Response{
        val response: Response = RestAssured.given()
            .baseUri(Environments.BASE_URL.url)
            .header("Content-Type", "application/json")
            .header("Accept", "application/json")
            .body(body)
            .`when`()
            .post("/project/${projectId}/step")
        return response
    }
    fun getJob(projectId: String,jobId:String?, requestBody:Map<String,Any?>):Response{
         return RestAssured.given()
             .baseUri(Environments.PROJECT_URL.url)
             .header("Content-Type", "application/json")
             .header("Accept", "application/json")
             .body(requestBody)
             .`when`()
        .get("/${projectId}/step/${jobId}")
    }
}
