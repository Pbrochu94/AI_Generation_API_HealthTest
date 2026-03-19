package utils.models

data class Project(var name:String,
                   var privacy:String,
                   var id:String? = null,
                   var createdAt: String? = null,
                   var updatedAt:String? = null,
                   var steps:List<Map<String,*>>? = null) {

    fun getParametersAsMap():Map<String,Any?> {
        return mutableMapOf(
            "name" to name,
            "privacy" to privacy,
            "id" to id,
            "createdAt" to createdAt,
            "updatedAt" to updatedAt,
            "steps" to steps
        )
    }
    fun generateProjectIds():String{
        return "${"%04d".format((0..9999).random())}-${"%04d".format((0..9999).random())}-${"%04d".format((0..9999).random())}"
    }
}