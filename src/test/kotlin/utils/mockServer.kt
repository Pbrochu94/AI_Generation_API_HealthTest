package utils

import com.github.tomakehurst.wiremock.client.WireMock.*
import com.github.tomakehurst.wiremock.WireMockServer
import java.util.Date
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig
import io.restassured.response.Response


object MockServer {
    private var online = false
    private val server = WireMockServer(
        wireMockConfig()
            .port(8089)
            .globalTemplating(true)
    )
    val baseUrl: String = "http://localhost:8089"
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
    val usersWithInvalidMail:List<Map<String,String>> = listOf(
        mapOf(
            "mail" to "Invalid@gmail.com",
            "username" to "Artorias",
            "password" to "abyssWalker01",
        ),
        mapOf(
            "mail" to "NotValid@gmail.com",
            "username" to "Master chief",
            "password" to "Spartan117",
        ),
        mapOf(
            "mail" to "Outdated.com",
            "username" to "Guts",
            "password" to "Berserker01",
        ),
    )
    val usersWithInvalidPassword:List<Map<String,String>> = listOf(
        mapOf(
            "mail" to "Artorias@gmail.com",
            "username" to "Artorias",
            "password" to "badpassword",
        ),
        mapOf(
            "mail" to "masterchief@gmail.com",
            "username" to "Master chief",
            "password" to "incorrectpassword",
        ),
        mapOf(
            "mail" to "Guts@gmail.com",
            "username" to "Guts",
            "password" to "outdatedPassword",
        ),
    )
    val usersWithInvalidCredentials:List<Map<String,String>> = listOf(
        mapOf(
            "mail" to "NotValid@gmail.com",
            "username" to "Artorias",
            "password" to "badpassword",
        ),
        mapOf(
            "mail" to "Invalid@gmail.com",
            "username" to "Master chief",
            "password" to "incorrectpassword",
        ),
        mapOf(
            "mail" to "Bad@gmail.com",
            "username" to "Guts",
            "password" to "outdatedPassword",
        ),
    )
    val projectsBaseInfo:List<Map<String,String>> = listOf(
        mapOf("name" to "Project Abyss", "privacy" to Privacy.PRIVATE.string),
        mapOf("name" to "Project Reach", "privacy" to Privacy.PUBLIC.string),
        mapOf("name" to "Project Eclipse", "privacy" to Privacy.PUBLIC.string),
        mapOf("name" to "Project Dragon", "privacy" to Privacy.PUBLIC.string),
        mapOf("name" to "Project Dream", "privacy" to Privacy.RESTRICTED.string),
        mapOf("name" to "Project Seireitei", "privacy" to Privacy.PRIVATE.string),
        mapOf("name" to "Project zeno", "privacy" to Privacy.PUBLIC.string),
        mapOf("name" to "Project Racoon City", "privacy" to Privacy.PUBLIC.string),
        mapOf("name" to "Project Grand Line", "privacy" to Privacy.RESTRICTED.string),
        mapOf("name" to "Project Metroid", "privacy" to Privacy.PUBLIC.string),
    )
    var projects: MutableList<Project> = mutableListOf()
    fun initDatabase() {
        projectsBaseInfo.forEach {
            val newProject = Project(it.getValue("name"), it.getValue("privacy"))
            generateProjectMetaData(newProject)
            addProjectToServer(newProject)
        }
    }
    fun generateProjectMetaData(project: Project) {
        project.id = project.generateProjectIds()
        project.createdAt = Date()
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
        validUsers.forEach { user ->
            server.stubFor(post("/login")
                .withRequestBody(matchingJsonPath("$.mail",  equalTo(user["mail"])))
                .withRequestBody(matchingJsonPath("$.password",  equalTo(user["password"])))
                .willReturn(aResponse()
                    .withStatus(201)
                    .withHeader("Content-Type", "application/json")
                    .withBody("""
                   {
                       "accountId": "01",
                       "accountName": "${user["username"]}",
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
            .willReturn(aResponse()
                .withStatus(201)
                .withHeader("Content-Type", "application/json")
                .withBody("""
                    {
                    "projectId": "${projects[0].id}",
                    "createdAt": "${projects[0].createdAt}",
                    "name": "{{jsonPath request.body '$.name'}}",
                    "privacy": "{{jsonPath request.body '$.privacy'}}"
                    },
                """.trimIndent())
            )
        )
        //get projects with VALID IDs
//        validProjectIds.forEach { id ->
//            server.stubFor(
//                get("/projects/$id")
//                    .willReturn(
//                        aResponse()
//                            .withStatus(200)
//                            .withHeader("Content-Type", "application/json")
//                            .withBody(
//                                """
//                                    {
//                                        "projectId": "$id",
//                                        "name": "Project $id"
//                                     }
//                                     """.trimIndent())
//                    )
//            )
//        }
        //get projects with INVALID IDs
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