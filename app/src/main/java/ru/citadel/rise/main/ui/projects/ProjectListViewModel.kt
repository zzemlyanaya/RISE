package ru.citadel.rise.main.ui.projects

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import kotlinx.coroutines.Dispatchers
import ru.citadel.rise.data.RemoteRepository
import ru.citadel.rise.data.model.Project
import ru.citadel.rise.data.model.Resource
import ru.citadel.rise.data.model.Result

class ProjectListViewModel : ViewModel() {
    private val repository = RemoteRepository()

    fun fetchAllData() = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            val result: Result<List<Project>> = repository.getAllProjects()
            if (result.error == null)
                emit(Resource.success(data = result.data))
            else
                emit(Resource.error(data = null, message = result.error))
        } catch (e: Exception){
            emit(Resource.error(data = null, message = e.message.toString()))
        }
    }

    fun fetchMyData(id: Int) = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            val result: Result<List<Project>> = repository.getMyProjectsById(id)
            if (result.error == null)
                emit(Resource.success(data = result.data))
            else
                emit(Resource.error(data = null, message = result.error))
        } catch (e: Exception){
            emit(Resource.error(data = null, message = e.message.toString()))
        }
    }

    fun fetchFavouriteData(id: Int) = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            val result: Result<List<Project>> = repository.getFavProjectsById(id)
            if (result.error == null)
                emit(Resource.success(data = result.data))
            else
                emit(Resource.error(data = null, message = result.error))
        } catch (e: Exception){
            emit(Resource.error(data = null, message = e.message.toString()))
        }
    }
}