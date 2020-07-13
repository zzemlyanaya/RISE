package ru.citadel.rise.login.registration

import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import ru.avangard.rise.R
import ru.citadel.rise.data.RemoteRepository
import ru.citadel.rise.data.model.User
import kotlin.coroutines.CoroutineContext

class RegistrationViewModel : ViewModel() {

    private val remoteRepository = RemoteRepository()

    private val _form = MutableLiveData<RegistrationFormState>()
    val registrationFormState: LiveData<RegistrationFormState> = _form

    private val parentJob = Job()
    private val coroutineContext: CoroutineContext
        get() = parentJob + Dispatchers.Default
    private val scope = CoroutineScope(coroutineContext)

    private val _isPersonChecked = MutableLiveData(true)
    private val _promptName = MutableLiveData("Ваше имя")

    val isPersonChecked: LiveData<Boolean> = _isPersonChecked
    val promptName: LiveData<String> = _promptName

    fun onPersonChecked(){
        _isPersonChecked.value = true
        _promptName.value = "Ваше имя"
    }

    fun onCompanyChecked(){
        _isPersonChecked.value = false
        _promptName.value = "Имя вашей компании"
    }

    fun createNew(user: User) = scope.launch {
        remoteRepository.addUser(user)
    }

    fun loginDataChanged(email: String, name: String, password: String) {
        if (!isEmailValid(email))
            _form.value =
                RegistrationFormState(emailError = R.string.invalid_email)
        else if (name.isBlank())
            _form.value =
                RegistrationFormState(emailError = null, nameError = R.string.invalid_username)
        else if (!isPasswordValid(password))
            _form.value =
                RegistrationFormState(emailError = null, nameError = null, passwordError = R.string.invalid_password)
        else {
            _form.value =
                RegistrationFormState(emailError = null,
                    nameError = null,
                    passwordError = null,
                    isDataValid = true)
        }
    }

    // A placeholder username validation check
    private fun isEmailValid(username: String) = Patterns.EMAIL_ADDRESS.matcher(username).matches()

    // A placeholder password validation check
    private fun isPasswordValid(password: String): Boolean {
        return password.length > 8
    }
}