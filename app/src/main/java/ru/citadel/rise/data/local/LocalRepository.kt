package ru.citadel.rise.data.local

import ru.citadel.rise.data.model.*

class LocalRepository(private val dao: IDaoLocal) {

    companion object {

        // For Singleton instantiation
        @Volatile private var instance: LocalRepository? = null

        fun getInstance(dao: IDaoLocal) =
            instance
                ?: synchronized(this) {
                    instance
                        ?: LocalRepository(
                            dao
                        )
                            .also { instance = it }
                }
    }

    // users
    fun getAllUsers() = dao.getAllUsers()

    fun insertUser(user: User) = dao.insertUser(user)

    fun updateUser(user: User) = dao.updateUser(user)

    fun deleteUser(user: User) = dao.deleteUser(user)

    // chats
    fun getAllUserChats(userId: Int) = dao.getAllUserChats(userId)

    fun insertChat(chats: List<ChatShortView>) = dao.insertChat(chats)

    fun insertUserChatRelation(userChatRelation: UserChatRelation) = dao.insertUserChatRelashion(userChatRelation)

    fun deleteChat(chats: List<ChatShortView>) = dao.deleteChat(chats)

    // projects
    fun insertProject(project: Project) = dao.insertProject(project)

    fun insertUserWithTheirProject(userWithTheirProject: UserWithTheirProjects) = dao.insertUserWithTheirProject(userWithTheirProject)

    fun insertUserWithFavProject(userWithFavProject: UserWithFavProjects) = dao.insertUserWithFavProject(userWithFavProject)

    fun updateProject(project: Project) = dao.updateProject(project)

    fun deleteProject(project: Project) = dao.deleteProject(project)

    fun deleteUserWithTheirProject(userWithTheirProject: UserWithTheirProjects) = dao.deleteUserWithTheirProject(userWithTheirProject)

    fun deleteUserWithFavProject(userWithFavProject: UserWithFavProjects) = dao.deleteUserWithFavProject(userWithFavProject)


}