package utils.data

import utils.models.User
import utils.server.MockServer

object UsersData{
    val userValidCredentials:List<User> = listOf(
        User("Artorias", "Artorias@gmail.com", "abyssWalker01"),
        User("Master chief", "masterchief@gmail.com", "Spartan117"),
        User("Guts", "Guts@gmail.com", "Berserker01"),
    )
    val userInvalidMail:List<Map<String,String>> = listOf(
        mapOf(
            "mail" to "Invalid@gmail.com",
            "username" to "Artorias",
            "password" to "abyssWalker01",
        ),
        mapOf(
            "mail" to "NotValid@gmail.com",
            "username" to "Master chief",
            "password" to "Spartan117",
        ),
        mapOf(
            "mail" to "Outdated.com",
            "username" to "Guts",
            "password" to "Berserker01",
        ),
    )
    val userInvalidPassword:List<Map<String,String>> = listOf(
        mapOf(
            "mail" to "Artorias@gmail.com",
            "username" to "Artorias",
            "password" to "badpassword",
        ),
        mapOf(
            "mail" to "masterchief@gmail.com",
            "username" to "Master chief",
            "password" to "incorrectpassword",
        ),
        mapOf(
            "mail" to "Guts@gmail.com",
            "username" to "Guts",
            "password" to "outdatedPassword",
        ),
    )
    val userInvalidCredentials:List<Map<String,String>> = listOf(
        mapOf(
            "mail" to "NotValid@gmail.com",
            "username" to "Artorias",
            "password" to "badpassword",
        ),
        mapOf(
            "mail" to "Invalid@gmail.com",
            "username" to "Master chief",
            "password" to "incorrectpassword",
        ),
        mapOf(
            "mail" to "Bad@gmail.com",
            "username" to "Guts",
            "password" to "outdatedPassword",
        ),
    )
    val tokenValid:List<String> = listOf(
        "mock-token-123",
        "mock-token-456",
        "mock-token-789",
        "mock-token-305",
        "mock-token-345",
        "mock-token-759",
        "mock-token-903",
    )
}