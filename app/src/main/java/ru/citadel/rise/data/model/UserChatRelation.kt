package ru.citadel.rise.data.model

import androidx.room.Entity
import androidx.room.Index

@Entity(tableName = "user_chat_relation", primaryKeys = ["userId", "chatId"],
        indices = [Index("userId"), Index("chatId")])
data class UserChatRelation(
    val userId: Int,
    val chatId: Int
)