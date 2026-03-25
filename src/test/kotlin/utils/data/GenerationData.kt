package utils.data

import utils.enums.Image2DFormat
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
    val image2DUrl:List<String> = listOf(
            "https://picsum.photos/seed/1/512/512",
            "https://picsum.photos/seed/2/512/512",
            "https://picsum.photos/seed/3/512/512",
            "https://picsum.photos/seed/8/512/512",
            "https://picsum.photos/seed/10/512/512"
    )
    enum class Image2DFormat(val string:String){
        PNG("png"),
        JPEG("jpg"),
    }
    enum class Status(val string:String){
        N_A("n/a"),
        IN_PROGRESS("in progress"),
        SUCCESS("succeeded"),
        FAILURE("failed"),
    }
}