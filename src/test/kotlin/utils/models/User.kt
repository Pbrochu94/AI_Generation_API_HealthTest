package utils.models

class User(val username:String,val mail:String,val password:String) {
    fun credentialsToMap(): Map<String, String> {
        return mapOf("mail" to mail, "password" to password)
    }
}