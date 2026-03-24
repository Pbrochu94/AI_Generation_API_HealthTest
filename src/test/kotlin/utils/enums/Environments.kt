package utils.enums

enum class Environments(val url:String) {
    BASE_URL("http://localhost:8089/"),
    LOGIN_URL("http://localhost:8089/login"),
    PROJECT_URL("http://localhost:8089/project"),
}