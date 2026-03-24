package utils.models

import utils.data.GenerationData
import utils.enums.Provider
import utils.enums.Status
import utils.enums.Tool
import kotlin.concurrent.thread

data class Generation (
    var id: String = generateJobId(),
    var provider:String = Provider.entries.random().string,
    var tool:String = Tool.entries.random().string,
    var prompt:String = GenerationData.validPromptList.random(),
    var status:String = Status.N_A.string,
    var progress: Int? = null,
    var output:Map<String,Any?>? = null
) {
    fun getRequestBody():MutableMap<String,Any?> {
        return mutableMapOf(
            "id" to id,
            "tool" to tool,
            "provider" to provider,
            "prompt" to prompt,
            "status" to status,
            "progress" to progress,
            "output" to output
        )
    }
    fun updateProgress(){
        if (progress == null) progress = 0
        else progress = progress!! + 25
        Thread.sleep(5000)
    }
    companion object{
        fun generateJobId():String{
            return "J001-${"%04d".format((0..9999).random())}-${"%04d".format((0..9999).random())}"
        }
    }
}