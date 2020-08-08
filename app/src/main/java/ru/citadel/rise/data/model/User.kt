package ru.citadel.rise.data.model

import kotlinx.serialization.Serializable


/**
 * Data class that captures user information for logged in users retrieved from LoginRepository
 * [id] is a hashcode of user's login
 */
@Serializable
data class User(
    val id: Int,
    var email: String,
    var name: String,
    var type: Int, //1 = person, 0 = company
    var age: Int?,
    var city: String?,
    var country: String?,
    var about: String?
)
