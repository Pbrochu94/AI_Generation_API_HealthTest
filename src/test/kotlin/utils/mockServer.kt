package utils

import com.github.tomakehurst.wiremock.client.WireMock.*
import com.github.tomakehurst.wiremock.WireMockServer
import java.util.Date
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper


object projectAPIData{
    val projectId:String = "123j9jk093803j3-22"
    val createdAt:Date = Date()
    val name:String = "Project"
}

object MockServer {
    private val server: WireMockServer = WireMockServer(8089)
    val baseUrl: String = "http://localhost:8089"
    fun start(){
        server.start()
        configureFor("localhost", 8089)
        //simulate endpoints
        server.stubFor(post("/projects")
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody("""
                    {
                    "projectId": ${projectAPIData.projectId},
                    "createdAt": ${projectAPIData.createdAt},
                    "name": ${projectAPIData.name},
                    },
                """.trimIndent())
            )

        )
    }
    fun stop(){
        server.stop()
    }
}