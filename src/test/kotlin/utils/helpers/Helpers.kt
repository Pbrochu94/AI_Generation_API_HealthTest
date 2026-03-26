package utils.helpers

import io.restassured.response.Response
import org.awaitility.Awaitility.await
import utils.enums.GenerationStatus
import utils.enums.Timer
import utils.helpers.RequestBuilder.getJob
import utils.models.Generation
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.concurrent.TimeUnit

fun today(): String {
    return LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
}

fun getResponseError(response:Response): String? {
    return response.body.jsonPath().getString("error")
}

fun generateRandomId(): String {
    return "${"%04d".format((0..9999).random())}-${"%04d".format((0..9999).random())}-${"%04d".format((0..9999).random())}"
}

fun pollGet(projectId:String, generation: Generation, timeout: Timer){
    await()
        .atMost(timeout.seconds, TimeUnit.SECONDS)
        .pollInterval(Timer.POLL_INTERVAL.seconds, TimeUnit.SECONDS)
        .until{
            val response  = getJob(projectId,generation.id, generation.getRequestBody())
                .then()
                .log().all()
                .extract()
                .response()
            val status = response.body.jsonPath().getString("status")
            status != GenerationStatus.IN_PROGRESS.string && status != GenerationStatus.N_A.string
        }
}
