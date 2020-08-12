package ru.citadel.rise.main.ui.chat

import java.io.Serializable

data class UserShortView(
    val id: Int,
    var name: String,
    var status: Int //1 = online, 0 = no
): Serializable