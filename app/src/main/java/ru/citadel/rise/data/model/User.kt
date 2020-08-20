package ru.citadel.rise.data.model

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import org.json.JSONObject
import java.io.Serializable


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
    var city: String?,
    @ColumnInfo
    var country: String?,
    @ColumnInfo
    var about: String?
) : Serializable {
    fun toJSON() = JSONObject(
        mapOf("userId" to userId, "email" to email, "name" to name, "type" to type,
        "city" to city, "country" to country, "about" to about)
    )
}
