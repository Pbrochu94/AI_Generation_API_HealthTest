package utils

import io.restassured.RestAssured
import io.restassured.response.Response
import io.restassured.specification.RequestSpecification
import utils.MockServer
import kotlin.text.get

object RequestBuilder{
    fun loginPost(user:Map<String,String> ): Response{
        val response: Response = RestAssured.given()
            .baseUri(MockServer.baseUrl)
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
            .baseUri(MockServer.baseUrl)
            .header("Content-Type", "application/json")
            .header("Accept", "application/json")
            .body(body)
            .`when`()
            .post("/project")
        return response
    }
    fun projectGet(id:String?): Response{
        val response: Response = RestAssured.given()
            .baseUri(MockServer.baseUrl)
            .header("Content-Type", "application/json")
            .header("Accept", "application/json")
            .`when`()
            .get("/project/${id}")
        return response
    }
    fun postStep(): RequestSpecification{
        val req: RequestSpecification = RestAssured.given()
            .baseUri(MockServer.baseUrl)
            .header("Content-Type", "application/json")
            .header("Accept", "application/json")
            .`when`()
        return req
    }
}
