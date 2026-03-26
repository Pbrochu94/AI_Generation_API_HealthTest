package utils.data

import utils.enums.Privacy
import utils.models.Project
import kotlin.collections.forEach

object ProjectData{
    val projects:List<Project> = listOf(
        Project("Abyss", Privacy.PRIVATE.string, GenerationData.jobList),
        Project("Reach", Privacy.PUBLIC.string, GenerationData.jobList),
        Project("Eclipse", Privacy.PUBLIC.string, GenerationData.jobList),
        Project("Dragon", Privacy.PUBLIC.string, GenerationData.jobList),
        Project("Dream", Privacy.RESTRICTED.string, GenerationData.jobList),
        Project("Seireitei", Privacy.PRIVATE.string, GenerationData.jobList),
        Project("Zeno", Privacy.PUBLIC.string,GenerationData.jobList),
        Project("Racoon City", Privacy.PUBLIC.string, GenerationData.jobList),
        Project("Grand Line", Privacy.RESTRICTED.string, GenerationData.jobList),
        Project("Metroid", Privacy.PUBLIC.string, GenerationData.jobList),
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
        val idList:MutableList<String> = mutableListOf()
        projects.forEach { project ->
            idList.add(project.id)
        }
        return idList
    }
}