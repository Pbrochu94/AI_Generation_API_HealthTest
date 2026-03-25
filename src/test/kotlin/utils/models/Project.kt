package utils.models

import utils.helpers.generateRandomId
import utils.helpers.today

data class Project(var name:String, var privacy:String, var steps:List<Generation>) {
    var id:String = generateRandomId()
    var createdAt: String = today()
    var updatedAt:String? = null
    fun getRequestBody():Map<String,Any?> {
        return mutableMapOf(
            "name" to name,
            "privacy" to privacy,
            "id" to id,
            "createdAt" to createdAt,
            "updatedAt" to updatedAt,
            "steps" to steps
        )
    }
}