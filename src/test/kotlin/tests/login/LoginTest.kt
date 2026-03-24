package tests.login

import io.restassured.response.Response
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import utils.models.BaseTest
import utils.helpers.RequestBuilder.loginPost
import utils.data.UsersData.userInvalidCredentials
import utils.data.UsersData.userInvalidMail
import utils.data.UsersData.userInvalidPassword
import utils.data.UsersData.userValidCredentials
import utils.helpers.getResponseError


class CredentialVerifications: BaseTest(){
    @Test
    fun `login with valid credentials returns 200`(){
        val response: Response = loginPost(userValidCredentials.random().credentialsToMap())
            .then()
            .log().all()
            .extract()
            .response()
        assertEquals(200, response.statusCode()){getResponseError(response)}
    }
    @Test
    fun `login with invalid password returns 401`(){
        val response:Response = loginPost(userInvalidPassword.random())
            .then()
            .log().all()
            .extract()
            .response()
        assertEquals(401, response.statusCode()){getResponseError(response)}
    }
    @Test
    fun `Login with invalid mail returns 401`(){
        val response:Response = loginPost(userInvalidMail.random())
            .then()
            .log().all()
            .extract()
            .response()
        assertEquals(401, response.statusCode()){getResponseError(response)}
    }
    @Test
    fun `Login with invalid mail and invalid password returns 401 and accurate error message`(){
        val response:Response = loginPost(userInvalidCredentials.random())
            .then()
            .log().all()
            .extract()
            .response()
        assertAll(
            {
                assertEquals(401, response.statusCode())
                assertEquals("Invalid credentials", getResponseError(response))
            }
        )

    }
}