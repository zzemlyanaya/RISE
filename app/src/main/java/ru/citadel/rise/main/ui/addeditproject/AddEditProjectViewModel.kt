package ru.citadel.rise.main.ui.addeditproject

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import kotlinx.coroutines.Dispatchers
import ru.citadel.rise.data.local.LocalRepository
import ru.citadel.rise.data.model.Project
import ru.citadel.rise.data.model.Resource
import ru.citadel.rise.data.model.UserWithTheirProjects
import ru.citadel.rise.data.remote.RemoteRepository

class AddEditProjectViewModel(
    private val localRepository: LocalRepository,
    private val userId: Int
) : ViewModel() {
    private val repository = RemoteRepository()

    fun addProject(project: Project) = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            val result = repository.addProject(project.toJSON())
            if(result.error == null) {
                try {
                    localRepository.updateProject(project)
                } catch (e1: Exception) {
                    localRepository.insertProject(listOf(project))
                    localRepository.insertUserWithTheirProject(
                        UserWithTheirProjects(
                            userId,
                            project.projectId
                        )
                    )
                }
                emit(Resource.success(data = result.data))
            }
            else
                emit(Resource.error(data = null, message = result.error))
        } catch (e: Exception) {
            emit(Resource.error(data = null, message = e.message.toString()))
        }
    }

    fun deleteProject(project: Project) = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            val result = repository.deleteProject(project.projectId)
            if(result.error == null) {
                localRepository.deleteProject(listOf(project))
                emit(Resource.success(data = result.data))
            }
            else
                emit(Resource.error(data = null, message = result.error))
        } catch (e: Exception) {
            emit(Resource.error(data = null, message = e.message.toString()))
        }
    }
}