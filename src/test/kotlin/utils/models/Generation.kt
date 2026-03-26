package utils.models

import kotlinx.coroutines.delay
import utils.data.GenerationData
import utils.enums.GenerationStatus
import utils.data.GenerationData.image2DUrl
import utils.enums.Image2DFormat
import utils.enums.Providers
import utils.enums.Tools

data class Generation (
    var id: String = generateJobId(),
    var provider:String = Providers.entries.random().string,
    var tool:String = Tools.entries.random().string,
    var prompt:String = GenerationData.subjectPromptList.random(),
    var status:String = GenerationStatus.N_A.string,
    var progress:Int =0,
    var imageUrl:String? = null,
    var format:String? = null
) {
    fun getRequestBody():MutableMap<String,Any?> {
        return mutableMapOf(
            "id" to id,
            "tool" to tool,
            "provider" to provider,
            "prompt" to prompt,
            "status" to status,
            "progress" to progress,
            "imageUrl" to imageUrl,
            "format" to format
        )
    }
    suspend fun generate(result:String = "success"){
        when(result.lowercase()){
            "success" -> {
                do {
                    delay(5000)
                    status = GenerationStatus.IN_PROGRESS.string
                    progress += 25
                }while(progress != 100)
                status = GenerationStatus.SUCCESS.string
                imageUrl = image2DUrl.random()
                format = Image2DFormat.entries.random().string
            }
            "failed" -> {
                do {
                    delay(5000)
                    status = GenerationStatus.IN_PROGRESS.string
                    progress += 25
                }while(progress != 75)
                status = GenerationStatus.FAILURE.string
                imageUrl = null
                format = null
            }
        }

    }
    companion object{
        fun generateJobId():String{
            return "J001-${"%04d".format((0..9999).random())}-${"%04d".format((0..9999).random())}"
        }
    }
}