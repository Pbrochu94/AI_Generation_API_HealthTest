package tests

import io.restassured.response.Response
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import utils.BaseTest
import utils.MockServer
import utils.RequestBuilder.loginPost
import utils.Users.userInvalidCredentials
import utils.Users.userInvalidMail
import utils.Users.userInvalidPassword
import utils.Users.userValidCredentials
import utils.getResponseError


class CredentialVerifications: BaseTest(){
    @Test
    fun `login with valid credentials returns 200`(){
        val response: Response = loginPost(userValidCredentials.random().credentialsToMap())
            .then()
            .log().all()
            .extract()
            .response()
        assertEquals(200, response.statusCode(), getResponseError(response))
    }
    @Test
    fun `login with invalid password returns 401`(){
        val response:Response = loginPost(userInvalidPassword.random())
            .then()
            .log().all()
            .extract()
            .response()
        assertEquals(401, response.statusCode(),getResponseError(response))
    }
    @Test
    fun `Login with invalid mail returns 401`(){
        val response:Response = loginPost(userInvalidMail.random())
            .then()
            .log().all()
            .extract()
            .response()
        assertEquals(401, response.statusCode(),getResponseError(response))
    }
    @Test
    fun `Login with invalid mail and invalid password returns 401`(){
        val response:Response = loginPost(userInvalidCredentials.random())
            .then()
            .log().all()
            .extract()
            .response()
        assertEquals(401, response.statusCode(),getResponseError(response))
    }
}