package utils.helpers

import io.restassured.response.Response
import java.time.LocalDate
import java.time.format.DateTimeFormatter

fun today(): String {
    return LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
}

fun getResponseError(response:Response): String? {
    return response.body.jsonPath().getString("error")
}
