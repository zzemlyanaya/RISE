package ru.citadel.rise.data.model

import kotlinx.serialization.Serializable


/**
 * Data class that captures user information for logged in users retrieved from LoginRepository
 * [id] is a hashcode of user's login
 */
@Serializable
data class User(
    val id: String,
    var name: String,
    var type: Int, //1 = author, 0 = company
    var age: Int?,
    var location: Location?,
    var projects: List<String>?, //list of project's array
    var qualifications: List<String>?, //list of random strings
    var about: String?
)

@Serializable
data class Location(
    var country: String,
    var city: String?
)