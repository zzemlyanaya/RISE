package ru.citadel.rise.login.data

import ru.citadel.rise.model.Location
import ru.citadel.rise.model.Result
import ru.citadel.rise.model.User
import java.io.IOException

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
class LoginDataSource {

    suspend fun login(login: String, password: String): Result<User> {
        try {
            Thread.sleep(2000)
            // TODO: handle loggedInUser authentication
            val fakeUser = User(java.util.UUID.randomUUID().toString(), "Jane", "Doe",
                1, 23, Location("Russia", "Yekaterinburg"), emptyList(),
                listOf("C#"), null
            )
            return Result.Success(fakeUser)
        } catch (e: Throwable) {
            return Result.Error(IOException("Error logging in", e))
        }
    }

    suspend fun logout() {
        // TODO: revoke authentication
    }
}