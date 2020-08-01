package ru.citadel.rise.data.model

data class Result<T>(
    val error: String?,
    val data: T?
)