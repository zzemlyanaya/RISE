package ru.citadel.rise.data.model

import org.json.JSONObject

data class Message(
    var id: Int?,
    var chatId: Int,
    var fromm: Int,
    var to: Int,
    var text: String,
    var time: String
) {
    fun toJSON() = JSONObject(
        mapOf(
            "id" to id,
            "chatId" to chatId,
            "fromm" to fromm,
            "to" to to,
            "text" to text,
            "time" to time
        )
    )
}