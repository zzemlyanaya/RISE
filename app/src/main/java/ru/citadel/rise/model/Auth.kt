package ru.citadel.rise.model

/**
 * Data calls used for handling authentication
 * [login]is user's login
 * [passwordToken]is token of user's password
 */

data class Auth(
    val login: String,
    val passwordToken: String
)