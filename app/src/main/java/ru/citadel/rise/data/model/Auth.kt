package ru.citadel.rise.data.model

/**
 * Data calls used for handling authentication
 * [login]is user's login
 * [passwordToken]is token of user's password
 */

data class Auth(
    val id: Int,
    val passwordToken: String
)