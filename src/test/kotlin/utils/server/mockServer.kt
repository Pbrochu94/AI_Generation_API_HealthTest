package utils.server

import com.github.tomakehurst.wiremock.client.WireMock.*
import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig
import utils.models.Project
import utils.data.ProjectData.populateValidBodyList
import utils.data.ProjectData.populateValidIdList
import utils.data.ProjectData.projectsBaseInfo
import utils.enums.Provider
import utils.enums.Tool
import utils.models.User
import utils.helpers.today


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
        projectsBaseInfo.forEach {
            val newProject = Project(it.getValue("name"), it.getValue("privacy"))
            generateProjectMetaData(newProject)
            populateValidBodyList(newProject.getParametersAsMap())
            populateValidIdList(newProject)
            addProjectToServer(newProject)
        }
    }
    fun generateProjectMetaData(project: Project) {
        project.id = project.generateProjectIds()
        project.createdAt = today()
    }
    fun addProjectToServer(newProject: Project) {
        projects.add(newProject)
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
        configureFor("localhost", 8089)
        //login VALID
        users.forEach { user ->
            server.stubFor(post("/login")
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
                                     }
                                     """.trimIndent())
                    )
            )
        }
        //get projects with INVALID IDs
        server.stubFor(
            get(urlPathMatching("/project/.*"))
                .atPriority(10)
                .willReturn(
                    aResponse()
                        .withStatus(404)
                        .withHeader("Content-Type", "application/json")
                        .withBody("""{"Details": "No project matching the required ID"}""")
                )
        )
        projects.forEach { project ->
            server.stubFor(post("/project/${project.id}/step")
                .withRequestBody(matchingJsonPath("$.provider", matching(Provider.providersToRegex())))
                .withRequestBody(matchingJsonPath("$.tool", matching(Tool.toolsToRegex())))
                .withRequestBody(matchingJsonPath("$.prompt"))
                .willReturn(aResponse()
                .withStatus(201)
                    .withHeader("Content-Type", "application/json")
                    .withBody("""
                        {
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
        server.stubFor(post(urlMatching("/project/.*/step"))
        .atPriority(10)
            .willReturn(aResponse()
                .withStatus(404)
                .withHeader("Content-Type", "application/json")
                .withBody("""{
                    "error": "No project matching the required ID"
                    }""".trimIndent())
            )
        )
    }
    fun stop(){
        server.stop()
    }
}