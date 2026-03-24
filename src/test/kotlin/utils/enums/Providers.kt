package utils.enums

enum class Providers(val string:String){
    GENV2("genv2"),
    NOTUAI("notuai"),
    MAKO("mako");
    companion object{
        fun providersToRegex():String{
            return entries.joinToString("|") { it.string }
        }
    }
}