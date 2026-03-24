package utils.data

import utils.models.Generation

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