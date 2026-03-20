package utils.models

data class Generation (
    var provider:String,
    var tool:String,
    var prompt:String,
    var status:String? = null,
    var progress:Int?=null,
    var output:Map<String,Any?>? = null
) {}