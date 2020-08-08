package ru.citadel.rise.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Tag (
    var id: Int,
    var name: String
)