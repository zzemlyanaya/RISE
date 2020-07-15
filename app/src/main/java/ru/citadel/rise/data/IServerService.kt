package ru.citadel.rise.data

import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import ru.citadel.rise.data.model.Project
import ru.citadel.rise.data.model.User

interface IServerService {
    companion object {
        const val CODE_SUCCESS = -1
        const val CODE_AUTH_ERROR = 1
        const val CODE_SERVER_ERROR = 2
        const val BASE_URL = "http://f15e7698cee8.ngrok.io/"
    }

    @GET("")
    fun getAllProjects(): List<Project>

    @GET("")
    fun getMyProjectsByUser(id: Int): List<Project>

    @GET("")
    fun getFavProjectsByUser(id: Int): List<Project>

    @POST("")
    fun addProject(project: Project)

    @PUT("")
    fun editProject(new: Project)

    @GET("")
    fun authorize(login: String, passwordToken: String): User

    @GET("")
    fun getUserById(id: Int): User

    @POST("")
    fun addUser(user: User)

    @PUT("")
    fun editUser(new: User)
}