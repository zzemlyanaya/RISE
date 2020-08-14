package ru.citadel.rise.data.local

import androidx.room.*
import ru.citadel.rise.data.model.*

@Dao
interface IDaoLocal {
    // users dao
    @Query("SELECT * FROM users")
    fun getAllUsers(): List<User>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertUser(user: User)

    @Update
    fun updateUser(user: User)

    @Delete
    fun deleteUser(user: User)

    // chats dao
    @Query("SELECT * FROM chats_short INNER JOIN user_chat_relation ON chats_short.chatId=user_chat_relation.chatId WHERE user_chat_relation.userId=:userId")
    fun getAllUserChats(userId: Int): List<ChatShortView>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertChat(chat: List<ChatShortView>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertUserChatRelashion(userChatRelation: UserChatRelation)

    @Delete
    fun deleteChat(chat: List<ChatShortView>)


    // projects dao
    @SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)
    @Query("SELECT * FROM projects INNER JOIN user_with_their_projects ON projects.projectId=user_with_their_projects.projectId WHERE user_with_their_projects.userId=:userId")
    fun getUserTheirProjects(userId: Int): List<Project>

    @SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)
    @Query("SELECT * FROM projects JOIN user_with_fav_projects ON projects.projectId=user_with_fav_projects.projectId WHERE user_with_fav_projects.userId=:userId")
    fun getUserFavProjects(userId: Int): List<Project>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertProject(project: Project)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertUserWithTheirProject(userWithTheirProject: UserWithTheirProjects)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertUserWithFavProject(userWithFavProject: UserWithFavProjects)

    @Update
    fun updateProject(project: Project)

    @Delete
    fun deleteProject(project: Project)

    @Delete
    fun deleteUserWithTheirProject(userWithTheirProject: UserWithTheirProjects)

    @Delete
    fun deleteUserWithFavProject(userWithFavProject: UserWithFavProjects)

}