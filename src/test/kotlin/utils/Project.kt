package utils

class Project(inputName:String,privacy: Privacy) {
    private var name:String = inputName
    private var privacy:Privacy = privacy
    private var id:String? = null
    private var createdAt:String? = null
    private var updatedAt:String? = null
    private var steps:List<Map<String,*>>? = null
    fun getName():String{return name}
    fun getPrivacy():String{return privacy.string}
}