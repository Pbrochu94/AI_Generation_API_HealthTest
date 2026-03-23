package utils.data

import kotlinx.coroutines.Job
import utils.enums.Privacy
import utils.models.Generation
import utils.models.Project
import utils.models.User
import utils.server.MockServer

object ProjectData{
    val projects:List<Project> = listOf(
        Project("Abyss", Privacy.PRIVATE.string, listOf(GenerationData.jobList.random())),
        Project("Reach", Privacy.PUBLIC.string, listOf(GenerationData.jobList.random(), GenerationData.jobList.random())),
        Project("Eclipse", Privacy.PUBLIC.string, listOf(GenerationData.jobList.random())),
        Project("Dragon", Privacy.PUBLIC.string, listOf(GenerationData.jobList.random(), GenerationData.jobList.random())),
        Project("Dream", Privacy.RESTRICTED.string, listOf(GenerationData.jobList.random(), GenerationData.jobList.random(),
            GenerationData.jobList.random(), GenerationData.jobList.random())),
        Project("Seireitei", Privacy.PRIVATE.string, listOf(GenerationData.jobList.random())),
        Project("Zeno", Privacy.PUBLIC.string),
        Project("Racoon City", Privacy.PUBLIC.string, listOf(GenerationData.jobList.random(), GenerationData.jobList.random())),
        Project("Grand Line", Privacy.RESTRICTED.string, listOf(GenerationData.jobList.random(), GenerationData.jobList.random(), GenerationData.jobList.random())),
        Project("Metroid", Privacy.PUBLIC.string, listOf(GenerationData.jobList.random())),
        )
    val projectIdValid:List<String> = populateValidIdList()
    val projectIdInvalid:List<String> = listOf(
        "1234-3445-3445",
        "3211-2345-6678",
        "3746-0960-3455",
        "1111-8599-6498",
        "1267-0990-2337",
        "0090-0690-4551",
        "0000-0000-0000"
    )
    fun populateValidIdList():List<String> {
        var idList:MutableList<String> = mutableListOf()
        projects.forEach { project ->
            idList.add(project.id)
        }
        return idList
    }
}

object UsersData{
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

object GenerationData{
    val validPromptList:List<String> = listOf(
        "a wolf",
        "a soldier",
        "a dragon",
        "villagers",
        "a car",
        "medieval weapons",
        "a monster with horns"
    )
    val jobList:List<Generation> = listOf(
        Generation(),
        Generation(),
        Generation(),
        Generation(),
        Generation(),
        Generation(),
        Generation(),
        Generation(),
        Generation(),
        Generation(),
        Generation(),
    )
}


