package ru.citadel.rise.data

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.citadel.rise.data.IServerService.Companion.BASE_URL
import ru.citadel.rise.data.model.Location
import ru.citadel.rise.data.model.Project
import ru.citadel.rise.data.model.User

class RemoteRepository {
    private val service: IServerService

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        service = retrofit.create(IServerService::class.java)
    }

    suspend fun getAllProjects() = service.getAllProjects()

    suspend fun getMyProjectsById(id: String) = service.getMyProjectsByUser(id)

    suspend fun getFavProjectsById(id: String) = service.getFavProjectsByUser(id)

//    suspend fun authorize(login: String, passwordToken: String)
//            = service.authorize(login, passwordToken)

    fun authorize(login: String, passwordToken: String) : User{
        return User(
            login.hashCode().toString(),
            login,
            0,
            null, Location("Russia", "Yekaterinburg"),
            listOf(""), listOf("App development"), null
        )
    }

    suspend fun getUserById(id: String) = service.getUserById(id)

    suspend fun addProject(project: Project) = service.addProject(project)

    suspend fun editProject(old: Project, new: Project) = service.editProject(old, new)

    //suspend fun addUser(user: User) = service.addUser(user)
    suspend fun addUser(user: User) {}

    suspend fun editUser(old: User, new: User) = service.editUser(old, new)
}