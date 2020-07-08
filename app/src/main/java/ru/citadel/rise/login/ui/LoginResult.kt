package ru.citadel.rise.login.ui

import ru.citadel.rise.model.User

/**
 * Authentication result : success (user details) or error message.
 */
data class LoginResult(
    val success: User? = null,
    val error: Int? = null
)