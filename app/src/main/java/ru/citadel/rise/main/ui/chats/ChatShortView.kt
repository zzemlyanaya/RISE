package ru.citadel.rise.main.ui.chats

data class ChatShortView(
    var chat_id: Int,
    var toID: Int,
    var toName: String,
    var lastMessage: String
)