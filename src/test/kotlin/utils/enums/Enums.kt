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

enum class Tool(val string:String){
    PROMPT_TO_IMAGE("text_to_image"),
    STYLE_RENDER("image_modification_style"),
    COLOR_CHANGE("image_modification_color"),
    ERASE("image_modification_erase");
    companion object{
        fun toolsToRegex():String{
            return values().joinToString("|") { it.string }
        }
    }
}

enum class Image2DFormat(val string:String){
    PNG("png"),
    JPEG("jpg"),
}
