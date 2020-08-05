package ru.citadel.rise.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Project(
    val id: Int,
    var name: String,
    var contact: Int, //contact user'
    var descriptionLong: String,
    var cost: String,
    var deadlines: String,
    var website: String?
) : java.io.Serializable