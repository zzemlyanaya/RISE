package ru.citadel.rise.data.remote

import org.json.JSONObject
import ru.citadel.rise.App
import ru.citadel.rise.data.model.Project
import ru.citadel.rise.data.model.Result
import ru.citadel.rise.data.model.User

class RemoteRepository {
    private val service = App.api

    private val fakeData = listOf(
        Project(1, "RISE", 1, "CITADEL",
                "The best startup platform ever",
            "1000000 рублей", "1 месяц", "http://bestApp.ever/RISE", "android,B2B,B2C,B2G,startup"),

        Project(2, "CITADEL Education", 1, "CITADEL",
            "The best education platform ever",
            "1100000 рублей", "2 месяца", "http://bestApp.ever/Education", "web,AI,neutral networks")
    )

    fun getServerStatus() = service.getServerStatus()

    fun getAllProjects() = service.getAllProjects()

    fun getMyProjectsById(id: Int) = service.getMyProjectsByUser(id)

    //suspend fun getFavProjectsById(id: Int) = service.getFavProjectsByUser(id)
    fun getFavProjectsById(id: Int) = Result(error= null, data = listOf(fakeData[0]))

    fun authorize(login: Int, passwordToken: Int)
            = service.authorize(login, passwordToken)

    fun registr(id: Int, name: String, passwordToken: Int, type: Int, email: String) : Result<User>
            = service.registr(id, name, passwordToken, type, email)

    fun getUserById(id: Int) = service.getUserById(id)

    fun addProject(project: JSONObject) = service.addProject(project)

    fun editProject(id: Int, new: Project) = service.editProject(id, new)

    fun editUser(id: Int, new: User) = service.editUser(id, new)

    fun getChatsByUser(id: Int) = service.getAllUserChats(id)

    fun getMessagesByChat(id: Int) = service.getMessagesByChat(id)

    fun addChat(user1: Int, user2: Int, lastMdg: String) = service.addChat(user1, user2, lastMdg)
}