package ru.citadel.rise.model

data class Project(
    val id: String,
    val name: String,
    val authors: List<String>, //list of author's ids
    val description: String,
    val cost: String,
    val deadlines: String
)