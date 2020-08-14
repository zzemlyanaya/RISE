package ru.citadel.rise.data.model

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable


@Serializable
@Entity(tableName = "users")
data class User(
    @PrimaryKey
    @NonNull
    @ColumnInfo
    val userId: Int,
    @NonNull
    @ColumnInfo
    var email: String,
    @NonNull
    @ColumnInfo
    var name: String,
    @NonNull
    @ColumnInfo
    var type: Int, //1 = person, 0 = company
    @ColumnInfo
    var age: Int?,
    @ColumnInfo
    var city: String?,
    @ColumnInfo
    var country: String?,
    @ColumnInfo
    var about: String?
)
