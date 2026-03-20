package utils.models
import com.github.tomakehurst.wiremock.extension.ResponseDefinitionTransformerV2
import com.github.tomakehurst.wiremock.http.*
import com.github.tomakehurst.wiremock.stubbing.ServeEvent
import com.github.tomakehurst.wiremock.client.ResponseDefinitionBuilder
import utils.server.MockServer

class JobResponseTransformer : ResponseDefinitionTransformerV2 {

    override fun transform(serveEvent: ServeEvent): ResponseDefinition {

        val job = MockServer.job

        val body = """
        {
          "provider": "${job?.provider}",
          "tool": "${job?.tool}",
          "prompt": "${job?.prompt}",
          "status": "${job?.status}",
          "progress": ${job?.progress},
          "output": ${job?.output}
        }
        """.trimIndent()

        return ResponseDefinitionBuilder()
            .withStatus(200)
            .withHeader("Content-Type", "application/json")
            .withBody(body)
            .build()
    }

    override fun getName() = "job-transformer"
}