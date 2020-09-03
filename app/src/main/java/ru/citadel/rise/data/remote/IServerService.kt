package ru.citadel.rise.data.remote

import org.json.JSONObject
import retrofit2.http.*
import ru.citadel.rise.data.model.*

interface IServerService {
    companion object {
        const val BASE_URL = "http://50a4a71bd630.ngrok.io"
    }

    @GET("/")
    fun getServerStatus(): Result<String>

    @GET("/projects")
    fun getAllProjects(): Result<List<Project>>

    @GET("/projects/my/{id}")
    fun getProjectsByUser(@Path("id")id: Int): Result<List<Project>>

    @GET("/projects/fav/{id}")
    fun getFavProjectsByUser(@Path("id")id: Int): Result<List<Project>>

    @POST("/projects")
    fun addProject(@Body project: JSONObject): Result<String>

    @POST("/projects/{id}")
    fun editProject(@Path("id")id: Int, new: JSONObject): Result<String>

    @DELETE("/projects/{id}")
    fun deleteProject(@Path("id")id: Int    ): Result<String>

    @GET("/login")
    fun authorize(@Query("id")id: Int,
                  @Query("passwordToken")passwordToken: Int,
                  @Query("keepAuth")keepAuth: Boolean): Result<User>

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

    @POST("/users")
    fun updateUser(@Body new: JSONObject): Result<String>

    @GET("/chats/by_user/{id}")
    fun getAllUserChats(@Path("id")id: Int): Result<List<ChatShortView>>

    @GET("/chats/{id}")
    fun getMessagesByChat(@Path("id")id: Int): Result<List<Message>>

    @POST("/chats")
    fun addChat(@Body json: JSONObject) : Result<ChatShortView>
}