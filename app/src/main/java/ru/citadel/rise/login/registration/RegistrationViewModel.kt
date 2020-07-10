package ru.citadel.rise.login.registration

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class RegistrationViewModel : ViewModel() {

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
}