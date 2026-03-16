package tests

import com.github.tomakehurst.wiremock.http.Response.response
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import utils.MockServer
import utils.RequestBuilder

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CredentialVerifications{
    @BeforeAll
    fun initServer(){
        MockServer.start()
    }
    @Test
    fun `Login with valid credentials returns 201`(){
        var response = RequestBuilder.login(MockServer.validUsers.random())
            .log().body()
            .`when`()
            .post("/login")
            .then()
            .statusCode(201)
            .extract()
        response()
        println(response.body().jsonPath().get<Map<String,String>>())
    }
    @Test
    fun `Login with invalid password returns 401`(){
        var response = RequestBuilder.login(MockServer.usersWithInvalidPassword.random())
            .log().body()
            .`when`()
            .post("/login")
            .then()
            .statusCode(401)
            .extract()
        response()
        println(response.body().jsonPath().getString("error"))
    }
    @Test
    fun `Login with invalid mail returns 401`(){
        var response = RequestBuilder.login(MockServer.usersWithInvalidMail.random())
            .log().body()
            .`when`()
            .post("/login")
            .then()
            .statusCode(401)
            .extract()
        response()
        println(response.body().jsonPath().getString("error"))
    }
    @Test
    fun `Login with invalid mail and invalid password returns 401`(){
        var response = RequestBuilder.login(MockServer.usersWithInvalidCredentials.random())
            .log().body()
            .`when`()
            .post("/login")
            .then()
            .statusCode(401)
            .extract()
        response()
        println(response.body().jsonPath().getString("error"))
    }
    @AfterAll
    fun destroyServer(){
        MockServer.stop()
    }
}