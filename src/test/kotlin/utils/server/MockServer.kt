package utils.server

import com.fasterxml.jackson.databind.ObjectMapper
import com.github.tomakehurst.wiremock.client.WireMock.*
import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig
import utils.models.Project
import utils.enums.Providers
import utils.enums.Tools
import utils.data.ProjectData.projects
import utils.data.UsersData.tokenValid
import utils.data.UsersData.userValidCredentials


object MockServer {
    private var online = false
    private val server = WireMockServer(
        wireMockConfig()
            .port(8089)
            .globalTemplating(true)
    )
    fun start(){
        if(!online){
            server.start()
            online = true
        }
        else{
            server.stop()
            online = false
        }
        wireMockConfig()
            .port(8089)
        //login VALID
        userValidCredentials.forEach { user ->
            server.stubFor(post("/login")
                .withName("login VALID")
                .withRequestBody(matchingJsonPath("$.mail",  equalTo(user.mail)))
                .withRequestBody(matchingJsonPath("$.password",  equalTo(user.password)))
                .willReturn(aResponse()
                    .withStatus(200)
                    .withHeader("Content-Type", "application/json")
                    .withBody("""
                   {
                       "token":"${tokenValid.random()}",
                       "user":{
                            "accountId": "01",
                            "accountName": "${user.username}"
                       }
                   }
               """.trimIndent())))
        }
        //login INVALID
        server.stubFor(post("/login")
            .withName("login INVALID")
            .withRequestBody(matchingJsonPath("$.mail"))
            .withRequestBody(matchingJsonPath("$.password"))
            .atPriority(10)
            .willReturn(aResponse()
                .withStatus(401)
                .withHeader("Content-Type", "application/json")
                .withBody("""
                   {
                       "error": "Invalid credentials"
                   }
               """.trimIndent())))
        //project CREATION
        server.stubFor(post("/project")
            .withName("project CREATION")
            .withRequestBody(matchingJsonPath("$.name"))
            .withRequestBody(matchingJsonPath("$.privacy"))
            .withRequestBody(matchingJsonPath("$.id"))
            .withRequestBody(matchingJsonPath("$.createdAt"))
            .willReturn(aResponse()
                .withStatus(201)
                .withHeader("Content-Type", "application/json")
                .withBody("""
                    {
                    "name": "{{jsonPath request.body '$.name'}}",
                    "privacy": "{{jsonPath request.body '$.privacy'}}",
                    "projectId": "{{jsonPath request.body '$.id'}}",
                    "createdAt": "{{jsonPath request.body '$.createdAt'}}",
                    "updatedAt": "{{jsonPath request.body '$.updatedAt'}}",
                    "steps": "{{jsonPath request.body '$.steps'}}"
                    },
                """.trimIndent())
            )
        )
        //POST project with invalid body
        server.stubFor(post("/project")
            .withName("POST project with invalid body")
            .atPriority(10)
            .willReturn(aResponse()
            .withStatus(400)
            .withHeader("Content-Type", "application/json")
            .withBody("""
                {
                "error": "Invalid request body"
                }
            """.trimIndent())
        )
        )

        //get projects with VALID IDs
        projects.forEach { project ->
            server.stubFor(
                get("/project/${project.id}")
                    .withName("get projects with VALID IDs")
                    .willReturn(
                        aResponse()
                            .withStatus(200)
                            .withHeader("Content-Type", "application/json")
                            .withBody(ObjectMapper().writeValueAsString(project))
                    )
            )
        }
        //GET projects with INVALID IDs
        server.stubFor(
            get(urlPathMatching("/project/.*"))
                .withName("get projects with INVALID IDs")
                .atPriority(10)
                .willReturn(
                    aResponse()
                        .withStatus(404)
                        .withHeader("Content-Type", "application/json")
                        .withBody("""{"error": "No project matching the required ID"}""")
                )
        )
        //Stub linked to every project endpoints
        projects.forEach { project ->
            //POST generation with correct project id and correct body parameters
            server.stubFor(post("/project/${project.id}/step")
                .withName("POST generation with correct project id and correct body parameters")
                .withRequestBody(matchingJsonPath("$.provider", matching(Providers.providersToRegex())))
                .withRequestBody(matchingJsonPath("$.tool", matching(Tools.toolsToRegex())))
                .withRequestBody(matchingJsonPath("$.prompt"))
                .willReturn(aResponse()
                    .withStatus(201)
                    .withHeader("Content-Type", "application/json")
                    .withBody(ObjectMapper().writeValueAsString(project))
                )
            )
            //POST generation with missing provider parameter
            server.stubFor(post("/project/${project.id}/step")
                .withName("POST generation with missing provider parameter")
                .withRequestBody(matchingJsonPath("$.tool", matching(Tools.toolsToRegex())))
                .withRequestBody(matchingJsonPath("$.prompt"))
                .withRequestBody(not(matchingJsonPath("$.provider")))
                .atPriority(8)
                .willReturn(aResponse()
                    .withStatus(400)
                    .withHeader("Content-Type", "application/json")
                    .withBody("""
                        {
                        "error": "Missing required 'provider' parameter in body"
                        }
                    """.trimIndent())
                )
            )
            //POST generation with wrong provider parameter
            server.stubFor(post("/project/${project.id}/step")
                .withName("POST generation with wrong provider parameter")
                .withRequestBody(matchingJsonPath("$.provider", notMatching(Providers.providersToRegex())))
                .withRequestBody(matchingJsonPath("$.tool", matching(Tools.toolsToRegex())))
                .withRequestBody(matchingJsonPath("$.prompt"))
                .atPriority(8)
                .willReturn(aResponse()
                    .withStatus(400)
                    .withHeader("Content-Type", "application/json")
                    .withBody("""
                        {
                        "error": "Provider selected is invalid"
                        }
                    """.trimIndent())
                )
            )
            //POST generation with missing tool parameter
            server.stubFor(post("/project/${project.id}/step")
                .withName("POST generation with missing tool parameter")
                .withRequestBody(matchingJsonPath("$.provider", matching(Providers.providersToRegex())))
                .withRequestBody(matchingJsonPath("$.prompt"))
                .withRequestBody(not(matchingJsonPath("$.tool")))
                .willReturn(aResponse()
                    .withStatus(400)
                    .withHeader("Content-Type", "application/json")
                    .withBody("""
                        {
                        "error": "Missing required 'tool' parameter in body"
                        }
                    """.trimIndent())
                )
            )
            //POST generation with wrong tool parameter
            server.stubFor(post("/project/${project.id}/step")
                .withName("POST generation with wrong tool parameter")
                .withRequestBody(matchingJsonPath("$.provider", matching(Providers.providersToRegex())))
                .withRequestBody(matchingJsonPath("$.tool", notMatching(Tools.toolsToRegex())))
                .withRequestBody(matchingJsonPath("$.prompt"))
                .atPriority(8)
                .willReturn(aResponse()
                    .withStatus(400)
                    .withHeader("Content-Type", "application/json")
                    .withBody("""
                        {
                        "error": "Tool selected is invalid"
                        }
                    """.trimIndent())
                )
            )
            //POST generation with wrong tool and provider parameter
            server.stubFor(post("/project/${project.id}/step")
                .withName("POST generation with wrong tool and provider parameter")
                .withRequestBody(matchingJsonPath("$.provider", notMatching(Providers.providersToRegex())))
                .withRequestBody(matchingJsonPath("$.tool", notMatching(Tools.toolsToRegex())))
                .withRequestBody(matchingJsonPath("$.prompt"))
                .atPriority(8)
                .willReturn(aResponse()
                    .withStatus(400)
                    .withHeader("Content-Type", "application/json")
                    .withBody("""
                        {
                        "error": "Provider and tool selected are invalid"
                        }
                    """.trimIndent())
                )
            )
            //POST generation with missing prompt parameter
            server.stubFor(post("/project/${project.id}/step")
                .withName("POST generation with missing prompt parameter")
                .withRequestBody(matchingJsonPath("$.provider", matching(Providers.providersToRegex())))
                .withRequestBody(matchingJsonPath("$.tool", matching(Tools.toolsToRegex())))
                .withRequestBody(not(matchingJsonPath("$.prompt")))
                .atPriority(8)
                .willReturn(aResponse()
                    .withStatus(400)
                    .withHeader("Content-Type", "application/json")
                    .withBody("""
                        {
                        "error": "Missing required 'prompt' parameter in body"
                        }
                    """.trimIndent())
                )
            )
            //Endpoints for each step generations
            project.steps.forEach { job ->
                //GET job with valid id
                server.stubFor(
                    get(urlEqualTo("/project/${project.id}/step/${job.id}"))
                        .withName("GET generation with correct id")
                        .atPriority(8)
                        .willReturn(
                            aResponse()
                                .withStatus(200)
                                .withBody("""
                                    {
                                        "id": "{{jsonPath request.body '$.id'}}",
                                        "provider": "{{jsonPath request.body '$.provider'}}",
                                        "tool": "{{jsonPath request.body '$.tool'}}",
                                        "prompt": "{{jsonPath request.body '$.prompt'}}",
                                        "status": "{{jsonPath request.body '$.status'}}",
                                        "progress": "{{jsonPath request.body '$.progress'}}",
                                        "imageUrl": "{{jsonPath request.body '$.imageUrl'}}",
                                        "format": "{{jsonPath request.body '$.format'}}",
                                    },
                """.trimIndent())
                        )
                )
                //GET job with invalid id
                server.stubFor(
                    get(urlEqualTo("/project/${project.id}/step/.*"))
                        .withName("GET generation with correct id")
                        .atPriority(10)
                        .willReturn(
                            aResponse()
                                .withStatus(200)
                                .withBody(
                                    """
                                        {
                                            "error": "Invalid job ID requested"
                                        }
                                    """.trimIndent()
                                )
                        )
                )
            }
        }
        //POST generation with incorrect project id
        server.stubFor(post(urlPathMatching("/project/.*/step"))
            .withName("POST generation with incorrect project id")
            .atPriority(10)
            .willReturn(aResponse()
                .withStatus(404)
                .withHeader("Content-Type", "application/json")
                .withBody("""
                       {
                        "error": "No project matching the required ID"
                       }
                   """.trimIndent())
            )
        )
    }
    fun stop(){
        server.stop()
    }
}