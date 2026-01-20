package utils

import com.github.tomakehurst.wiremock.client.WireMock.*
import com.github.tomakehurst.wiremock.WireMockServer
import java.util.Date
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper



object MockServer {
    private val server: WireMockServer = WireMockServer(8089)
    val baseUrl: String = "http://localhost:8089"
    val validProjectIds:List<String> = listOf("123j9jk093803j3-22","123gdhnjetehetsl-45", "dsadasfasfsdasda-55","fdhdfgsdgsdffsdf231-22")
    val createdAt:Date = Date()
    val validUsers:List<Map<String,String>> = listOf(
        mapOf(
            "mail" to "Artorias@gmail.com",
            "username" to "Artorias",
            "password" to "abyssWalker01",
        ),
        mapOf(
            "mail" to "masterchief@gmail.com",
            "username" to "Master chief",
            "password" to "Spartan117",
        ),
        mapOf(
            "mail" to "Guts@gmail.com",
            "username" to "Guts",
            "password" to "Berserker01",
        ),
    )
    val projectName:List<String> = listOf("Project Abyss","Project Reach", "Project Eclipse", "Project Dragon", "Project Dream", "Project Seireitei")
    fun start(){
        server.start()
        configureFor("localhost", 8089)
        //simulate endpoints
        validUsers.forEach { user ->
            server.stubFor(post("/login")
                .withRequestBody(matchingJsonPath("$.mail",  equalTo(user["mail"])))
                .withRequestBody(matchingJsonPath("$.password",  equalTo(user["password"])))
                .willReturn(aResponse()
                    .withHeader("Content-Type", "application/json")
                    .withBody("""
                   {
                       "accountId": "01",
                       "accountName": "Account Name",
                   }
               """.trimIndent())))
        }
        server.stubFor(post("/login")
            .withRequestBody(matchingJsonPath("$.mail"))
            .withRequestBody(matchingJsonPath("$.password"))
            .atPriority(10)
            .willReturn(aResponse()
                .withHeader("Content-Type", "application/json")
                .withBody("""
                   {
                       "error": "Invalid credentials"
                   }
               """.trimIndent())))

        server.stubFor(post("/projects")
            .willReturn(aResponse()
                .withStatus(201)
                .withHeader("Content-Type", "application/json")
                .withBody("""
                    {
                    "projectId": ${validProjectIds.random()},
                    "createdAt": ${createdAt},
                    "name": ${projectName.random()},
                    },
                """.trimIndent())
            )
        )
        // Stub for valid IDs
        validProjectIds.forEach { id ->
            server.stubFor(
                get("/projects/$id")
                    .willReturn(
                        aResponse()
                            .withStatus(200)
                            .withHeader("Content-Type", "application/json")
                            .withBody(
                                """
                                    {
                                        "projectId": "$id",
                                        "name": "Project $id"
                                     }
                                     """.trimIndent())
                    )
            )
        }

        // Generic stub for invalid IDs
        server.stubFor(
            get(urlPathMatching("/projects/.*"))
                .atPriority(10) // priorité plus basse
                .willReturn(
                    aResponse()
                        .withStatus(404)
                        .withHeader("Content-Type", "application/json")
                        .withBody("""{"Details": "No project matching the required ID"}""")
                )
        )
    }
    fun stop(){
        server.stop()
    }
}