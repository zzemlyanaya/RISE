package ru.citadel.rise.data

import retrofit2.http.*
import ru.citadel.rise.data.model.Project
import ru.citadel.rise.data.model.Result
import ru.citadel.rise.data.model.User

interface IServerService {
    companion object {
        const val BASE_URL = "http://50a7fbda1e7f.ngrok.io"
    }

    @GET("/")
    fun getServerStatus(): Result<String>

    @GET("/projects")
    fun getAllProjects(): Result<List<Project>>

    @GET("/projects/my/{id}")
    fun getMyProjectsByUser(@Path("id")id: Int): Result<List<Project>>

    @GET("/projects/fav/{id}")
    fun getFavProjectsByUser(@Path("id")id: Int): Result<List<Project>>

    @POST("/projects")
    fun addProject(project: Project): Result<String>

    @PUT("/projects/{id}")
    fun editProject(@Path("id")id: Int, new: Project): Result<String>

    @GET("/login")
    fun authorize(@Query("id")id: Int,
                  @Query("passwordToken")passwordToken: Int): Result<User>

    @GET("/registr")
    fun registr(@Query("id")id: Int,
                @Query("name")name: String,
                @Query("passwordToken")token: Int,
                @Query("type")type: Int,
                @Query("email")email: String): Result<User>

    @GET("/logout")
    fun logout(): Result<String>

    @GET("/users/{id}")
    fun getUserById(@Path("id")id: Int): Result<User>

    @PUT("/users/{id}")
    fun editUser(@Path("id")id:Int, new: User): Result<String>
}