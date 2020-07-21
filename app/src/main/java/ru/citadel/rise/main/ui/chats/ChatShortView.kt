package ru.citadel.rise.main.ui.chats

data class ChatShortView(
    var currentUser: Int, //id
    var toID: Int, //id
    var toName: String,
    var lastMessage: String
)