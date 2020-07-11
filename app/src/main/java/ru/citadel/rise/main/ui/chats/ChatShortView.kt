package ru.citadel.rise.main.ui.chats

data class ChatShortView(
    var from: String, //id
    var to: String, //id
    var lastMessage: String,
    var lastMessageTime: String
)