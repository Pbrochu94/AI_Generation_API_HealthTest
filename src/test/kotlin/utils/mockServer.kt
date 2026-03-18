package utils

import com.github.tomakehurst.wiremock.client.WireMock.*
import com.github.tomakehurst.wiremock.WireMockServer
import java.util.Date
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig
import io.restassured.response.Response
import utils.ProjectData.populateTestValidBodyList
import utils.ProjectData.projectsBaseInfo
import java.time.format.DateTimeFormatter


object MockServer {
    private var online = false
    private val server = WireMockServer(
        wireMockConfig()
            .port(8089)
            .globalTemplating(true)
    )
    val baseUrl: String = "http://localhost:8089"
    val users:List<User> = listOf(
        User("Artorias","Artorias@gmail.com","abyssWalker01"),
        User("Master chief","masterchief@gmail.com","Spartan117"),
        User("Guts","Guts@gmail.com","Berserker01"),
    )
    var projects: MutableList<Project> = mutableListOf()
    fun initDatabase() {
        projectsBaseInfo.forEach {
            val newProject = Project(it.getValue("name"), it.getValue("privacy"))
            generateProjectMetaData(newProject)
            populateTestValidBodyList(newProject.getParametersAsMap())
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
                "error": "Invalid body request"
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
    }
    fun stop(){
        server.stop()
    }
}