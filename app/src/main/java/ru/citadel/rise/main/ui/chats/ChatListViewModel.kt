package ru.citadel.rise.main.ui.chats

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import kotlinx.coroutines.Dispatchers
import ru.citadel.rise.data.RemoteRepository
import ru.citadel.rise.data.model.Resource
import ru.citadel.rise.data.model.Result

class ChatListViewModel : ViewModel() {
    private val repository = RemoteRepository()

    fun fetchAllChats(id: Int) = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            val result: Result<List<ChatShortView>> = repository.getChatsByUser(id)
            if (result.error == null)
                emit(Resource.success(data = result.data))
            else
                emit(Resource.error(data = null, message = result.error))
        } catch (e: Exception){
            emit(Resource.error(data = null, message = e.message.toString()))
        }
    }
}