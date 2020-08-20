package ru.citadel.rise.main.ui.projects

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.citadel.rise.data.local.LocalRepository
import ru.citadel.rise.data.model.Project
import ru.citadel.rise.data.model.Resource
import ru.citadel.rise.data.model.Result
import ru.citadel.rise.data.model.UserWithTheirProjects
import ru.citadel.rise.data.remote.RemoteRepository

class ProjectListViewModel(private val localRepository: LocalRepository) : ViewModel() {
    private val repository = RemoteRepository()

    fun fetchAllProjects() = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            val result: Result<List<Project>> = repository.getAllProjects()
            if (result.error == null) {
                emit(Resource.success(data = result.data))
                localRepository.insertProject(result.data!!)
            }
            else
                emit(Resource.error(data = null, message = result.error))
        } catch (e: Exception){
            emit(Resource.error(data = null, message = e.message.toString()))
        }
    }

    fun fetchProjectsByUser(id: Int) = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            val result: Result<List<Project>> = repository.getProjectsByUser(id)
            if (result.error == null)
                emit(Resource.success(data = result.data))
            else
                emit(Resource.error(data = null, message = result.error))
        } catch (e: Exception){
            emit(Resource.error(data = null, message = e.message.toString()))
        }
    }
//
//    fun fetchFavouriteData(id: Int) = liveData(Dispatchers.IO) {
//        emit(Resource.loading(data = null))
//        try {
//            val result: Result<List<Project>> = repository.getFavProjectsById(id)
//            if (result.error == null)
//                emit(Resource.success(data = result.data))
//            else
//                emit(Resource.error(data = null, message = result.error))
//        } catch (e: Exception){
//            emit(Resource.error(data = null, message = e.message.toString()))
//        }
//    }

    fun fetchAllProjectsLocally() = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(localRepository.getAllProjects()))
        } catch (e: Exception){
            emit(Resource.error(data = null, message = e.message.toString()))
        }
    }

    fun fetchMyProjectsLocally(id: Int) = liveData(Dispatchers.IO){
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(localRepository.getUserWithTheirProjects(id)))
        } catch (e: Exception){
            emit(Resource.error(data = null, message = e.message.toString()))
        }
    }

    fun fetchFavProjectsLocally(id: Int) = liveData(Dispatchers.IO){
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(localRepository.getUserWithFavProjects(id)))
        } catch (e: Exception){
            emit(Resource.error(data = null, message = e.message.toString()))
        }
    }

    fun updateMyProjects(userId: Int, projects: List<Project>) = run {
        CoroutineScope(Dispatchers.IO).launch {
            for(i in projects)
                localRepository.insertUserWithTheirProject(UserWithTheirProjects(userId, i.projectId))
        }
    }
}