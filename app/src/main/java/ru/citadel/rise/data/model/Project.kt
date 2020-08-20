package ru.citadel.rise.data.model

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import org.json.JSONObject

@Entity(tableName = "projects")
data class Project(
    @PrimaryKey
    @NonNull
    @ColumnInfo
    val projectId: Int,
    @NonNull
    @ColumnInfo
    var name: String,
    @NonNull
    @ColumnInfo
    var contact: Int, //contact user'
    @NonNull
    @ColumnInfo
    var contactName: String, //name only for view
    @NonNull
    @ColumnInfo
    var descriptionLong: String,
    @ColumnInfo
    var cost: String?,
    @ColumnInfo
    var deadlines: String?,
    @ColumnInfo
    var website: String?,
    @ColumnInfo
    var tags: String?
) : java.io.Serializable {

    fun toJSON() = JSONObject(mapOf(
        "id" to projectId, "name" to name, "contact" to contact, "contactName" to contactName,
        "descriptionLong" to descriptionLong, "cost" to cost, "deadlines" to deadlines,
        "website" to website, "tags" to tags
    ))
}