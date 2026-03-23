package utils.models

import kotlin.concurrent.thread

data class Generation (
    var id: Int,
    var provider:String,
    var tool:String,
    var prompt:String,
    var status:String? = null,
    var progress: Int? = null,
    var output:Map<String,Any?>? = null
) {
    fun updateProgress(){
        if (progress == null) progress = 0
        else progress = progress!! + 25
        Thread.sleep(5000)
    }
}