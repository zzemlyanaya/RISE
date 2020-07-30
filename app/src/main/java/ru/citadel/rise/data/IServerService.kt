package ru.citadel.rise.data

import retrofit2.http.*
import ru.citadel.rise.data.model.Project
import ru.citadel.rise.data.model.User

interface IServerService {
    companion object {
        const val BASE_URL = " http://6b49ed6f3536.ngrok.io"
    }

    @GET("/projects")
    fun getAllProjects(): List<Project>

    @GET("/projects/my/{id}")
    fun getMyProjectsByUser(@Path("id")id: Int): List<Project>

    @GET("/projects/fav/{id}")
    fun getFavProjectsByUser(@Path("id")id: Int): List<Project>

    @POST("/projects")
    fun addProject(project: Project)

    @PUT("/projects/{id}")
    fun editProject(@Path("id")id: Int, new: Project)

    @GET("/login")
    fun authorize(@Query("id")id: Int, @Query("passwordToken")passwordToken: String): User

    @GET("/registr")
    fun registr(@Query("id")id: Int,
                @Query("name")name: String,
                @Query("passwordToken")token: Int,
                @Query("type")type: Int,
                @Query("email")email: String): User

    @GET("/logout")
    fun logout()

    @GET("/users/{id}")
    fun getUserById(@Path("id")id: Int): User

    @PUT("/users/{id}")
    fun editUser(@Path("id")id:Int, new: User)
}