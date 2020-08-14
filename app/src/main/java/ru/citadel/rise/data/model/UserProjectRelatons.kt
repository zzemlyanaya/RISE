package ru.citadel.rise.data.model

import androidx.room.Entity

@Entity(tableName = "user_with_fav_projects", primaryKeys = ["userId", "projectId"])
data class UserWithFavProjects(
    val userId: Int,
    val projectId: Int
)


@Entity(tableName = "user_with_their_projects", primaryKeys = ["userId", "projectId"])
data class UserWithTheirProjects(
    val userId: Int,
    val projectId: Int
)
