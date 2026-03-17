package utils

import java.util.Date

data class Project(var name:String,
                   var privacy:String,
                   var id:String? = null,
                   var createdAt: Date? = null,
                   var updatedAt:Date? = null,
                   var steps:List<Map<String,*>>? = null) {

    fun getBaseInfoAsMap():Map<String,String> {
        return mapOf("name" to name, "privacy" to privacy)
    }
    fun generateProjectIds():String{
        return "${"%04d".format((0..9999).random())}-${"%04d".format((0..9999).random())}-${"%04d".format((0..9999).random())}"
    }
}