package utils.server

import com.github.tomakehurst.wiremock.client.WireMock.*
import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig
import utils.models.Project
import utils.enums.Provider
import utils.enums.Tool
import utils.models.User
import utils.data.ProjectData


object MockServer {
    private var online = false
    private val server = WireMockServer(
        wireMockConfig()
            .port(8089)
            .globalTemplating(true)
    )
    val baseUrl: String = "http://localhost:8089"
    val users:List<User> = listOf(
        User("Artorias", "Artorias@gmail.com", "abyssWalker01"),
        User("Master chief", "masterchief@gmail.com", "Spartan117"),
        User("Guts", "Guts@gmail.com", "Berserker01"),
    )
    var projects: MutableList<Project> = mutableListOf()
    fun initDatabase() {
        ProjectData.projects.forEach { project ->
            projects.add(project)
        }
    }
    fun start(){
        if(!online){
            server.start()
            online = true
        }
        else{
            server.stop()
            online = false
        }
        initDatabase()
        wireMockConfig()
            .port(8089)
        //login VALID
        users.forEach { user ->
            server.stubFor(post("/login")
                .withName("login VALID")
                .withRequestBody(matchingJsonPath("$.mail",  equalTo(user.mail)))
                .withRequestBody(matchingJsonPath("$.password",  equalTo(user.password)))
                .willReturn(aResponse()
                    .withStatus(200)
                    .withHeader("Content-Type", "application/json")
                    .withBody("""
                   {
                       "accountId": "01",
                       "accountName": "${user.username}",
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
                            .withBody(
                                """
                                    {
                                        "name": "${project.name}",
                                        "privacy": "${project.privacy}",
                                       "projectId": "${project.id}",
                                        "createdAt": "${project.createdAt}",
                                        "updatedAt": "${project.updatedAt}",
                                        "steps": "${project.steps}",
                                     }
                                     """.trimIndent())
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
        //POST generation with correct project id and correct body parameters
        projects.forEach { project ->
            server.stubFor(post("/project/${project.id}/step")
                .withName("POST generation with correct project id and correct body parameters")
                .withRequestBody(matchingJsonPath("$.provider", matching(Provider.providersToRegex())))
                .withRequestBody(matchingJsonPath("$.tool", matching(Tool.toolsToRegex())))
                .withRequestBody(matchingJsonPath("$.prompt"))
                .willReturn(aResponse()
                    .withStatus(201)
                    .withHeader("Content-Type", "application/json")
                    .withBody("""
                        {
                        "id": "${project.id}",
                        "provider": ""{{jsonPath request.body '$.provider'}}"",
                        "tool": ""{{jsonPath request.body '$.tool'}}"",
                        "prompt": ""{{jsonPath request.body '$.prompt'}}"",
                        "status": "In progress",
                        "output":[]
                        }
                    """.trimIndent())
                )
            )
        }
        //POST generation with missing provider parameter
        projects.forEach { project ->
            server.stubFor(post("/project/${project.id}/step")
                .withName("POST generation with missing provider parameter")
                .withRequestBody(matchingJsonPath("$.tool", matching(Tool.toolsToRegex())))
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
        }
        //POST generation with wrong provider parameter
        projects.forEach { project ->
            server.stubFor(post("/project/${project.id}/step")
                .withName("POST generation with wrong provider parameter")
                .withRequestBody(matchingJsonPath("$.provider", notMatching(Provider.providersToRegex())))
                .withRequestBody(matchingJsonPath("$.tool", matching(Tool.toolsToRegex())))
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
        }
        //POST generation with missing tool parameter
        projects.forEach { project ->
            server.stubFor(post("/project/${project.id}/step")
                .withName("POST generation with missing tool parameter")
                .withRequestBody(matchingJsonPath("$.provider", matching(Provider.providersToRegex())))
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
        }
        //POST generation with wrong tool parameter
        projects.forEach { project ->
            server.stubFor(post("/project/${project.id}/step")
                .withName("POST generation with wrong tool parameter")
                .withRequestBody(matchingJsonPath("$.provider", matching(Provider.providersToRegex())))
                .withRequestBody(matchingJsonPath("$.tool", notMatching(Tool.toolsToRegex())))
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
        }
        //POST generation with wrong tool and provider parameter
        projects.forEach { project ->
            server.stubFor(post("/project/${project.id}/step")
                .withName("POST generation with wrong tool and provider parameter")
                .withRequestBody(matchingJsonPath("$.provider", notMatching(Provider.providersToRegex())))
                .withRequestBody(matchingJsonPath("$.tool", notMatching(Tool.toolsToRegex())))
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
        }
        //POST generation with missing prompt parameter
        projects.forEach { project ->
            server.stubFor(post("/project/${project.id}/step")
                .withName("POST generation with missing prompt parameter")
                .withRequestBody(matchingJsonPath("$.provider", matching(Provider.providersToRegex())))
                .withRequestBody(matchingJsonPath("$.tool", matching(Tool.toolsToRegex())))
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
        server.stubFor(get(urlPathMatching("/project/.*/step")))
    }
    fun stop(){
        server.stop()
    }
}