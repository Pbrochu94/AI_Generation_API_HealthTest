package tests.login

import io.restassured.response.Response
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import utils.models.BaseTest
import utils.helpers.RequestBuilder.loginPost
import utils.data.Users.userInvalidCredentials
import utils.data.Users.userInvalidMail
import utils.data.Users.userInvalidPassword
import utils.data.Users.userValidCredentials
import utils.helpers.getResponseError


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