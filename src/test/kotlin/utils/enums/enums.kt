package utils.enums

enum class Privacy(val string:String){
    PUBLIC("public"),
    PRIVATE("private"),
    RESTRICTED("restricted")
}

enum class Provider(val string:String){
    GENV2("genv2"),
    NOTUAI("notuai"),
    MAKO("mako");
    companion object{
        fun providersToRegex():String{
            return values().joinToString("|") { it.string }
        }
    }
}

enum class Category(val string:String){}