package ru.citadel.rise.model

import kotlinx.serialization.Serializable

@Serializable
data class Project(
    val id: String,
    val name: String,
    val authors: List<String>, //list of author's ids
    val descriptionShort: String,
    val descriptionLong: String,
    val cost: String,
    val deadlines: String
) : java.io.Serializable