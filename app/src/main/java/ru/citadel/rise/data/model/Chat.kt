package ru.citadel.rise.data.model

data class Chat(
    private var id: Int,
    private var user1: Int,
    private var user2: Int,
    private var lastMessage: String
)