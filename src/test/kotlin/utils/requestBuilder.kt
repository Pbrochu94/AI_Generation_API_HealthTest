package utils

import io.restassured.RestAssured
import io.restassured.response.Response
import io.restassured.specification.RequestSpecification
import utils.MockServer

object RequestBuilder{
    fun loginBuilder(user:Map<String,String> ): RequestSpecification{
        val req: RequestSpecification = RestAssured.given()
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
        return req
    }
    fun projectBuilder(body:Map<String,Any?>): RequestSpecification {
        val req: RequestSpecification = RestAssured.given()
            .baseUri(MockServer.baseUrl)
        .header("Content-Type", "application/json")
        .header("Accept", "application/json")
            .body(body)
            .`when`()
        return req
    }
}
