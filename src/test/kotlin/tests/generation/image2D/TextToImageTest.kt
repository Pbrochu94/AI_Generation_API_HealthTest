package tests.generation.image2D

import com.github.tomakehurst.wiremock.http.Response.response
import io.restassured.response.Response
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import utils.data.GenerationData
import utils.data.GenerationData.Status
import utils.data.ProjectData
import utils.models.BaseTest
import utils.data.ProjectData.projectIdValid
import utils.enums.Providers
import utils.helpers.RequestBuilder.getJob
import utils.helpers.RequestBuilder.postStep
import utils.models.Generation
import kotlin.concurrent.thread
import kotlin.test.assertNotNull
import kotlin.test.assertTrue


class GenV2: BaseTest() {
    @Test
    fun `test`(){
        val project = ProjectData.projects.random()
        val generation = project.steps.random()
        var response:Response? = null
        println(generation)
        CoroutineScope(Dispatchers.Default).launch {
            generation.generate("success")
        }
        do {
            response  = getJob(project.id,generation.id, generation.getRequestBody())
                .then()
                .log().all()
                .extract()
                .response()
            println(response.asString())
            val status = response.body.jsonPath().getString("status")
        }while ( status == Status.IN_PROGRESS.string ||status == Status.N_A.string)
        val completedResponse:Response = getJob(project.id,generation.id, generation.getRequestBody())
            .then()
            .log().all()
            .extract()
            .response()
        println(generation)
        println(completedResponse.asString())
    }
    @Test
    fun `Generation succeed and returns output`(){
        val project = ProjectData.projects.random()
        val response: Response = getJob(project.id, project.steps.random().id, Generation(provider = Providers.GENV2.string).getRequestBody())
            .then()
            .log().all()
            .extract()
            .response()
        assertAll(
            {
                assertEquals(200, response.statusCode)
                assertEquals(0, response.body.jsonPath().getInt("progress"))
            }
        )
    }
}