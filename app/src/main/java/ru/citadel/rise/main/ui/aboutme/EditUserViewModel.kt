package ru.citadel.rise.main.ui.aboutme

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import kotlinx.coroutines.Dispatchers
import ru.citadel.rise.data.local.LocalRepository
import ru.citadel.rise.data.model.Resource
import ru.citadel.rise.data.model.User
import ru.citadel.rise.data.remote.RemoteRepository

class EditUserViewModel(private val localRepository: LocalRepository) : ViewModel() {
    private val remoteRepository = RemoteRepository()

    fun updateUser(user: User) = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            localRepository.updateUser(user)
            remoteRepository.updateUser(user.toJSON())
            emit(Resource.success(data = "OK"))
        } catch (e: Exception) {
            emit(Resource.error(data = null, message = e.message.toString()))
        }
    }
}