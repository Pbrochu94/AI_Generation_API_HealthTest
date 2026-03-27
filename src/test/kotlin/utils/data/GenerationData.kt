package utils.data



import utils.models.Generation

object GenerationData{
    val subjectPromptList:List<String> = listOf(
        "a wolf",
        "a soldier",
        "a dragon",
        "villagers",
        "a car",
        "medieval weapons",
        "a monster with horns"
    )
    val colorPromptList:List<String> = listOf(
        "Change blue to black",
        "Change red to yellow",
        "Change red to green",
        "Change green to blue",
        "Change red to white",
        "Change black to purple",
        "Change gray to white",
    )
    val eraseSubjectList:List<String> = listOf(
        "Remove the horns",
        "Remove the sword",
        "Remove the ears",
        "Remove the background",
        "Remove the sun",
        "Erase the wheels",
        "Erase the ears",
    )
    val jobList:MutableList<Generation> = mutableListOf(
        Generation(
            id = "J001-0987-0900",
            provider = Providers.GENV2.string,
            tool = Tools.PROMPT_TO_IMAGE.string,
            prompt = subjectPromptList.random(),
            status = Status.N_A.string,
            progress = 0,
            imageUrl = null,
            format = null
        ),
        Generation(
            id = "J001-1234-2344",
            provider = Providers.GENV2.string,
            tool = Tools.PROMPT_TO_IMAGE.string,
            prompt = subjectPromptList.random(),
            status = Status.N_A.string,
            progress = 0,
            imageUrl = null,
            format = null
        ),
        Generation(
            id = "J001-4455-5467",
            provider = Providers.MAKO.string,
            tool = Tools.PROMPT_TO_IMAGE.string,
            prompt = subjectPromptList.random(),
            status = Status.N_A.string,
            progress = 0,
            imageUrl = null,
            format = null
        ),
        Generation(
            id = "J001-2333-4444",
            provider = Providers.NOTUAI.string,
            tool = Tools.PROMPT_TO_IMAGE.string,
            prompt = subjectPromptList.random(),
            status = Status.N_A.string,
            progress = 0,
            imageUrl = null,
            format = null
        ),
        Generation(
            id = "J001-1111-2322",
            provider = Providers.GENV2.string,
            tool = Tools.COLOR_CHANGE.string,
            prompt = colorPromptList.random(),
            status = Status.N_A.string,
            progress = 0,
            imageUrl = null,
            format = null
        ),
        Generation(
            id = "J001-9999-1254",
            provider = Providers.MAKO.string,
            tool = Tools.COLOR_CHANGE.string,
            prompt = colorPromptList.random(),
            status = Status.N_A.string,
            progress = 0,
            imageUrl = null,
            format = null
        ),
        Generation(
            id = "J001-4555-5676",
            provider = Providers.NOTUAI.string,
            tool = Tools.COLOR_CHANGE.string,
            prompt = colorPromptList.random(),
            status = Status.N_A.string,
            progress = 0,
            imageUrl = null,
            format = null
        ),
        Generation(
            id = "J001-0900-3222",
            provider = Providers.GENV2.string,
            tool = Tools.STYLE_RENDER.string,
            prompt = subjectPromptList.random(),
            status = Status.N_A.string,
            progress = 0,
            imageUrl = null,
            format = null
        ),
        Generation(
            id = "J001-2222-2322",
            provider = Providers.NOTUAI.string,
            tool = Tools.STYLE_RENDER.string,
            prompt = subjectPromptList.random(),
            status = Status.N_A.string,
            progress = 0,
            imageUrl = null,
            format = null
        ),
        Generation(
            id = "J001-1111-2234",
            provider = Providers.MAKO.string,
            tool = Tools.STYLE_RENDER.string,
            prompt = subjectPromptList.random(),
            status = Status.N_A.string,
            progress = 0,
            imageUrl = null,
            format = null
        ),
        Generation(
            id = "J001-1111-2234",
            provider = Providers.GENV2.string,
            tool = Tools.ERASE.string,
            prompt = eraseSubjectList.random(),
            status = Status.N_A.string,
            progress = 0,
            imageUrl = null,
            format = null
        ),
        Generation(
            id = "J001-3333-4344",
            provider = Providers.MAKO.string,
            tool = Tools.ERASE.string,
            prompt = eraseSubjectList.random(),
            status = Status.N_A.string,
            progress = 0,
            imageUrl = null,
            format = null
        ),
        Generation(
            id = "J001-7555-5677",
            provider = Providers.NOTUAI.string,
            tool = Tools.ERASE.string,
            prompt = eraseSubjectList.random(),
            status = Status.N_A.string,
            progress = 0,
            imageUrl = null,
            format = null
        ),
    )
    val image2DUrl:List<String> = listOf(
            "https://picsum.photos/seed/1/512/512",
            "https://picsum.photos/seed/2/512/512",
            "https://picsum.photos/seed/3/512/512",
            "https://picsum.photos/seed/8/512/512",
            "https://picsum.photos/seed/10/512/512"
    )
    enum class Image2DFormat(val string:String){
        PNG("png"),
        JPEG("jpg"),
    }
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
    enum class Status(val string:String) {
        N_A("n/a"),
        IN_PROGRESS("in progress"),
        SUCCESS("succeeded"),
        FAILURE("failed"),
    }

}