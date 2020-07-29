package ru.citadel.rise.data

import android.util.Log
import ru.citadel.rise.App
import ru.citadel.rise.data.model.Location
import ru.citadel.rise.data.model.Project
import ru.citadel.rise.data.model.User

class RemoteRepository {
    private val service = App.api

    private val fakeData = listOf(
        Project(1, "RISE", 1, "The best startup platform ever",
            "Some very long text which i definitely don't won't to type so it's kinda short text",
            "1000000 рублей", "1 месяц", "http://bestApp.ever/RISE"
        ),
        Project(2, "CITADEL Education", 1, "The best education platform ever",
            "Some very long text which i definitely don't won't to type so it's kinda short text",
            "1100000 рублей", "2 месяца", "http://bestApp.ever/Education"
        )
    )

    suspend fun getAllProjects() = service.getAllProjects()
    //suspend fun getAllProjects() = fakeData

    suspend fun getProjectByID(id: Int) = service.getProjectByID(id)

    //suspend fun getMyProjectsById(id: Int) = service.getMyProjectsByUser(id)
    suspend fun getMyProjectsById(id: Int) = fakeData

    //suspend fun getFavProjectsById(id: Int) = service.getFavProjectsByUser(id)
    suspend fun getFavProjectsById(id: Int) = listOf(fakeData[0])

//    suspend fun authorize(login: String, passwordToken: String)
//            = service.authorize(login, passwordToken)

    fun authorize(email: String, passwordToken: String) : User{
        Log.i("AUTH", email.hashCode().toString()+"SF"+passwordToken)

        return User(
            email.hashCode(),
            email,
            "CITADEL",
            0,
            null, Location("Russia", "Yekaterinburg"),
            listOf(1), null
        )
    }

    suspend fun getUserById(id: Int) = service.getUserById(id)

    suspend fun addProject(project: Project) = service.addProject(project)

    suspend fun editProject(new: Project) = service.editProject(new)

    //suspend fun addUser(user: User) = service.addUser(user)
    suspend fun addUser(user: User) {}

    suspend fun editUser(new: User) = service.editUser( new)
}