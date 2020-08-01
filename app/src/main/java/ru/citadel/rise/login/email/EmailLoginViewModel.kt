package ru.citadel.rise.login.email

import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import kotlinx.coroutines.Dispatchers
import ru.avangard.rise.R
import ru.citadel.rise.data.RemoteRepository
import ru.citadel.rise.data.model.Resource
import ru.citadel.rise.data.model.Result
import ru.citadel.rise.data.model.User

class EmailLoginViewModel : ViewModel() {
    private val repository = RemoteRepository()

    private val _loginForm = MutableLiveData(LoginFormState())
    val loginFormState: LiveData<LoginFormState> = _loginForm

    fun authorize(id: Int, password: String) = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            val result: Result<User> = repository.authorize(id, password.hashCode())
            if (result.error == null)
                emit(Resource.success(data = result.data))
            else
                emit(Resource.error(data = null, message = result.error))
        } catch (e: Exception) {
            emit(Resource.error(data = null, message = e.message ?: "Ошибка сервера! Попробуйте снова."))
        }
    }

    fun loginDataChanged(email: String, password: String) {
        _loginForm.value =
            LoginFormState(usernameError = validateEmail(email),
                passwordError = validatePassword(password),
                isDataValid = isAllDataValid(email, password))
    }

    private fun isAllDataValid(email: String, password: String)
            = Patterns.EMAIL_ADDRESS.matcher(email).matches() and (password.length >= 8)

    private fun validateEmail(email: String) =
        if (Patterns.EMAIL_ADDRESS.matcher(email).matches()) null else R.string.invalid_email


    private fun validatePassword(password: String) =
        if (password.length >= 8) null else R.string.invalid_password
}