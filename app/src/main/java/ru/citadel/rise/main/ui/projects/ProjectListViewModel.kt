package ru.citadel.rise.main.ui.projects

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import kotlinx.coroutines.Dispatchers
import ru.citadel.rise.data.RemoteRepository
import ru.citadel.rise.data.model.Resource

class ProjectListViewModel : ViewModel() {
    private val repository = RemoteRepository()

    fun fetchAllData() = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = repository.getAllProjects()))
        } catch (e: Exception){
            emit(Resource.error(data = null, message = e.message.toString()))
        }
    }

    fun fetchMyData(id: Int) = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = repository.getMyProjectsById(id)))
        } catch (e: Exception){
            emit(Resource.error(data = null, message = e.message.toString()))
        }
    }

    fun fetchFavouriteData(id: Int) = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = repository.getFavProjectsById(id)))
        } catch (e: Exception){
            emit(Resource.error(data = null, message = e.message.toString()))
        }
    }
}