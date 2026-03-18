package tests

import io.restassured.RestAssured
import io.restassured.response.Response
import org.eclipse.jetty.server.Request
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import utils.MockServer
import utils.RequestBuilder
import utils.RequestBuilder.loginBuilder
import utils.getResponseError
import utils.usersWithInvalidCredentials
import utils.usersWithInvalidMail
import utils.usersWithInvalidPassword

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CredentialVerifications{
    @BeforeAll
    fun initServer(){
        MockServer.start()
    }
    @Test
    fun `login with valid credentials returns 200`(){
        val response: Response = loginBuilder(MockServer.validUsers.random())
            .post("/login")
            .then()
            .log().all()
            .extract()
            .response()
        assertEquals(200, response.statusCode(), getResponseError(response))
    }
    @Test
    fun `login with invalid password returns 401`(){
        val response:Response = loginBuilder(usersWithInvalidPassword.random())
            .post("/login")
            .then()
            .log().all()
            .extract()
            .response()
        assertEquals(401, response.statusCode(),getResponseError(response))
    }
    @Test
    fun `Login with invalid mail returns 401`(){
        val response:Response = loginBuilder(usersWithInvalidMail.random())
            .post("/login")
            .then()
            .log().all()
            .extract()
            .response()
        assertEquals(401, response.statusCode(),getResponseError(response))
    }
    @Test
    fun `Login with invalid mail and invalid password returns 401`(){
        val response:Response = loginBuilder(usersWithInvalidCredentials.random())
            .post("/login")
            .then()
            .log().all()
            .extract()
            .response()
        assertEquals(401, response.statusCode(),getResponseError(response))
    }
    @AfterAll
    fun destroyServer(){
        MockServer.stop()
    }
}