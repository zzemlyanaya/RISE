package ru.citadel.rise.data.local

import androidx.room.*
import ru.citadel.rise.data.model.*

@Dao
interface ILocalDao {
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
    @Query("SELECT * FROM projects WHERE contact=:userId")
    fun getUserTheirProjects(userId: Int): List<Project>

    @Query("SELECT projectId, name, contact, contactName, descriptionLong, cost, deadlines, website, tags FROM projects JOIN user_with_fav_projects ON projects.projectId=user_with_fav_projects.projId WHERE user_with_fav_projects.userId=:userId")
    fun getUserFavProjects(userId: Int): List<Project>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertProjects(projects: List<Project>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertUserWithTheirProject(userWithTheirProject: UserWithTheirProjects)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertUserWithFavProject(userWithFavProject: UserWithFavProjects)

    @Update
    fun updateProject(project: Project)

    @Delete
    fun deleteProjects(projects: List<Project>)

    @Delete
    fun deleteUserWithTheirProject(userWithTheirProject: UserWithTheirProjects)

    @Delete
    fun deleteUserWithFavProject(userWithFavProject: UserWithFavProjects)

}