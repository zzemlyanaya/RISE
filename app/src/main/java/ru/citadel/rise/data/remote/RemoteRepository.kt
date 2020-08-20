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

    fun authorize(login: Int, passwordToken: Int, keepAuth: Boolean)
            = service.authorize(login, passwordToken, keepAuth)

    fun logout() = service.logout()

    fun registr(id: Int, name: String, passwordToken: Int, type: Int, email: String) : Result<User>
            = service.registr(id, name, passwordToken, type, email)

    fun getUserById(id: Int) = service.getUserById(id)

    fun addProject(project: JSONObject) = service.addProject(project)

    fun editProject(id: Int, new: JSONObject) = service.editProject(id, new)

    fun deleteProject(id: Int) = service.deleteProject(id)

    fun updateUser(new: JSONObject) = service.updateUser(new)

    fun getChatsByUser(id: Int) = service.getAllUserChats(id)

    fun getMessagesByChat(id: Int) = service.getMessagesByChat(id)

    fun addChat(json: JSONObject) = service.addChat(json)
}