package utils

import org.eclipse.jetty.server.Authentication

object ProjectData{
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
    val invalidProjectId:List<String> = listOf(
        "1234-3445-3445",
        "3211-2345-6678",
        "3746-0960-3455",
        "1111-8599-6498",
        "1267-0990-2337",
        "0090-0690-4551",
        "0000-0000-0000"
    )
    val projectValidBody:MutableList<Map<String,Any?>> = mutableListOf()
    fun populateTestValidBodyList(projectBody:Map<String,Any?>) {
        projectValidBody.add(projectBody)
    }
}

object Users{
    val userValidCredentials:List<User> = MockServer.users
    val userInvalidMail:List<Map<String,String>> = listOf(
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
    val userInvalidPassword:List<Map<String,String>> = listOf(
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
    val userInvalidCredentials:List<Map<String,String>> = listOf(
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
}

