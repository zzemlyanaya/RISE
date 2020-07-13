package ru.citadel.rise.data

import retrofit2.http.GET
import retrofit2.http.POST
import ru.citadel.rise.data.model.Project
import ru.citadel.rise.data.model.User

interface IServerService {
    companion object {
        const val CODE_SUCCESS = -1
        const val CODE_AUTH_ERROR = 1
        const val CODE_SERVER_ERROR = 2
        const val BASE_URL = "https://github.com/"
    }

    @GET("")
    fun getAllProjects(): List<Project>

    @GET("")
    fun getMyProjectsByUser(id: String): List<Project>

    @GET("")
    fun getFavProjectsByUser(id: String): List<Project>

    @POST("")
    fun addProject(project: Project)

    @POST("")
    fun editProject(old: Project, new: Project)

    @GET("")
    fun authorize(login: String, passwordToken: String): User

    @GET("")
    fun getUserById(id: String): User

    @POST("")
    fun addUser(user: User)

    @POST("")
    fun editUser(old: User, new: User)
}