package ru.citadel.rise.main.ui.chat

data class UserShortView(
    val id: Int,
    var name: String,
    var status: Int //1 = online, 0 = no
)