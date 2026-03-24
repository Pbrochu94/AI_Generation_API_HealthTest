package tests.project

import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.assertEquals
import utils.data.ProjectData
import utils.models.BaseTest
import utils.data.ProjectData.projectIdInvalid
import utils.data.ProjectData.projectIdValid
import utils.helpers.RequestBuilder.projectCreationPost
import utils.helpers.RequestBuilder.projectGet
import utils.helpers.getResponseError


class ProjectEndpoint: BaseTest() {
    @Test
    fun `POST call with valid body to project endpoint return 201` (){
        val response = projectCreationPost(ProjectData.projects.random().getRequestBody())
            .then()
            .log().all()
            .extract()
            .response()
        assertEquals(201, response.statusCode())
    }
    @Test
    fun `POST call with invalid body to project endpoint return 400 and accurate error message` (){
        val response = projectCreationPost(mapOf<String,String>("Color" to "Black"))
            .then()
            .log().all()
            .extract()
            .response()
        assertAll(
            {
                assertEquals(400, response.statusCode())
                assertEquals("Invalid request body", getResponseError(response))
            }
        )

    }
    @Test
    fun `GET call with valid project id return 200`(){
        val response = projectGet(projectIdValid.random())
            .then()
            .log().all()
        .extract()
        .response()
        assertEquals(200, response.statusCode())
    }
    @Test
    fun `GET call with invalid project id return 404 and accurate error message`(){
        val response = projectGet(projectIdInvalid.random())
            .then()
            .log().all()
            .extract()
            .response()
        assertAll(
            {
                assertEquals(404, response.statusCode())
                assertEquals("No project matching the required ID", getResponseError(response))
            }
        )
    }
}