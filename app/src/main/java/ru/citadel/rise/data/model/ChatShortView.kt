package ru.citadel.rise.data.model

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "chats_short")
data class ChatShortView(
   @PrimaryKey
   @NonNull
   @ColumnInfo
   val chatId: Int,
   @NonNull
   @ColumnInfo
   val userId: Int,
   @NonNull
   @ColumnInfo
   val toID: Int,
   @NonNull
   @ColumnInfo
   val toName: String,
   @NonNull
   @ColumnInfo
   val lastMessage: String
)