package ru.citadel.rise.model

import kotlinx.serialization.Serializable


/**
 * Data class that captures user information for logged in users retrieved from LoginRepository
 * [id] is a hashcode of user's login
 */
@kotlinx.serialization.Serializable
data class User(
    val id: String,
    val name: String,
    val type: Int, //1 = author, 0 = company
    val age: Int?,
    val location: Location?,
    val projects: List<String>?, //list of project's array
    val qualifications: List<String>?, //list of random strings
    val about: String?
)

@Serializable
data class Location(
    val country: String,
    val city: String?
)