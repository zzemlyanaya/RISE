package ru.citadel.rise.data.remote

import org.json.JSONObject
import ru.citadel.rise.App
import ru.citadel.rise.data.model.Result
import ru.citadel.rise.data.model.User

class RemoteRepository {
    private val service = App.api


    fun getServerStatus() = service.getServerStatus()

    fun getAllProjects() = service.getAllProjects()

    fun getProjectsByUser(id: Int) = service.getProjectsByUser(id)

    fun getFavProjectsByUser(id: Int) = service.getFavProjectsByUser(id)

    fun authorize(login: Int, passwordToken: Int)
            = service.authorize(login, passwordToken)

    fun registr(id: Int, name: String, passwordToken: Int, type: Int, email: String) : Result<User>
            = service.registr(id, name, passwordToken, type, email)

    fun getUserById(id: Int) = service.getUserById(id)

    fun addProject(project: JSONObject) = service.addProject(project)

    fun editProject(id: Int, new: JSONObject) = service.editProject(id, new)

    fun deleteProject(id: Int) = service.deleteProject(id)

    fun editUser(id: Int, new: User) = service.editUser(id, new)

    fun getChatsByUser(id: Int) = service.getAllUserChats(id)

    fun getMessagesByChat(id: Int) = service.getMessagesByChat(id)

    fun addChat(user1: Int, user2: Int, lastMdg: String) = service.addChat(user1, user2, lastMdg)
}