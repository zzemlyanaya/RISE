package ru.citadel.rise.data.model

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "chats")
data class Chat(
    @PrimaryKey
    @NonNull
    @ColumnInfo
    var id: Int,
    @NonNull
    @ColumnInfo
    var user1: Int,
    @NonNull
    @ColumnInfo
    var user2: Int,
    @NonNull
    @ColumnInfo
    var lastMessage: String
)