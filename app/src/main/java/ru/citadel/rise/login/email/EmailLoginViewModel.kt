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

class EmailLoginViewModel : ViewModel() {
    private val repository = RemoteRepository()

    private val _loginForm = MutableLiveData<LoginFormState>()
    val loginFormState: LiveData<LoginFormState> = _loginForm

    fun authorize(login: String, password: String) = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = repository.authorize(login, password.hashCode().toString())))
        } catch (e: Exception) {
            emit(Resource.error(data = null, message = e.message ?: "Ошибка сервера! Попробуйте снова."))
        }
    }

    fun loginDataChanged(username: String, password: String) {
        if (!isUserNameValid(username)) {
            _loginForm.value =
                LoginFormState(usernameError = R.string.invalid_username)
        } else if (!isPasswordValid(password)  ) {
            _loginForm.value =
                LoginFormState(usernameError = null, passwordError = R.string.invalid_password)
        } else {
            _loginForm.value =
                LoginFormState(usernameError = null,
                    passwordError = null,
                    isDataValid = true)
        }
    }

    // A placeholder username validation check
    private fun isUserNameValid(username: String): Boolean {
        return if (username.contains('@')) {
            Patterns.EMAIL_ADDRESS.matcher(username).matches()
        } else {
            username.isNotBlank()
        }
    }

    // A placeholder password validation check
    private fun isPasswordValid(password: String): Boolean {
        return password.length > 8
    }
}