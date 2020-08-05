package ru.citadel.rise.data

import ru.citadel.rise.App
import ru.citadel.rise.data.model.Project
import ru.citadel.rise.data.model.Result
import ru.citadel.rise.data.model.User

class RemoteRepository {
    private val service = App.api

    private val fakeData = listOf(
        Project(1, "RISE", 1, "The best startup platform ever",
            "1000000 рублей", "1 месяц", "http://bestApp.ever/RISE"
        ),
        Project(2, "CITADEL Education", 1, "The best education platform ever",
            "1100000 рублей", "2 месяца", "http://bestApp.ever/Education"
        )
    )

    fun getServerStatus() = service.getServerStatus()

    suspend fun getAllProjects() = service.getAllProjects()
    //suspend fun getAllProjects() = fakeData

    suspend fun getMyProjectsById(id: Int) = service.getMyProjectsByUser(id)
    //suspend fun getMyProjectsById(id: Int) = fakeData

    //suspend fun getFavProjectsById(id: Int) = service.getFavProjectsByUser(id)
    suspend fun getFavProjectsById(id: Int) = Result(error= null, data = listOf(fakeData[0]))

    suspend fun authorize(login: Int, passwordToken: Int)
            = service.authorize(login, passwordToken)

//    fun authorize(email: String, passwordToken: String) : User{
//        Log.i("AUTH", email.hashCode().toString()+"SF"+passwordToken)
//
//        return User(
//            email.hashCode(),
//            email,
//            "CITADEL",
//            0,
//            null, Location("Russia", "Yekaterinburg"),
//            listOf(1), null
//        )
//    }

    suspend fun registr(id: Int, name: String, passwordToken: Int, type: Int, email: String) : Result<User>
            = service.registr(id, name, passwordToken, type, email)

    suspend fun getUserById(id: Int) = service.getUserById(id)

    suspend fun addProject(project: Project) = service.addProject(project)

    suspend fun editProject(id: Int, new: Project) = service.editProject(id, new)

    suspend fun editUser(id: Int, new: User) = service.editUser(id, new)
}