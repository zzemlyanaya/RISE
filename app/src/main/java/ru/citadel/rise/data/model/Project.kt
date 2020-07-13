package ru.citadel.rise.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Project(
    val id: String,
    var name: String,
    var authors: List<String>, //list of author's ids
    var descriptionShort: String,
    var descriptionLong: String,
    var cost: String,
    var deadlines: String
) : java.io.Serializable