package utils.enums

enum class Tools(val string: String) {
    PROMPT_TO_IMAGE("text_to_image"),
    STYLE_RENDER("image_modification_style"),
    COLOR_CHANGE("image_modification_color"),
    ERASE("image_modification_erase");
    companion object{
        fun toolsToRegex():String{
            return Tools.entries.joinToString("|") { it.string }
        }
    }
}