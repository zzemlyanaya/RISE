package ru.citadel.rise.main.ui.addeditproject

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import kotlinx.coroutines.Dispatchers
import ru.citadel.rise.data.model.Project
import ru.citadel.rise.data.model.Resource
import ru.citadel.rise.data.remote.RemoteRepository

class AddEditProjectViewModel : ViewModel() {
    private val repository = RemoteRepository()

    fun addProject(project: Project) = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            val result = repository.addProject(project.toJSON())
            if(result.error == null)
                emit(Resource.success(data = result.data))
            else
                emit(Resource.error(data = null, message = result.error))
        } catch (e: Exception) {
            emit(Resource.error(data = null, message = e.message.toString()))
        }
    }
}