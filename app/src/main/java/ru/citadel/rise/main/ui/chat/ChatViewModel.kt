package ru.citadel.rise.main.ui.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import kotlinx.coroutines.Dispatchers
import ru.citadel.rise.data.model.Message
import ru.citadel.rise.data.model.Resource
import ru.citadel.rise.data.model.Result
import ru.citadel.rise.data.remote.RemoteRepository


class ChatViewModel : ViewModel() {

    private val repository = RemoteRepository()

    private val curChatId = 0

    fun fetchChatMessages(chatId: Int) = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            val result: Result<List<Message>> = repository.getMessagesByChat(chatId)
            if (result.error == null)
                emit(Resource.success(data = result.data))
            else
                emit(Resource.error(data = null, message = result.error))
        } catch (e: Exception){
            emit(Resource.error(data = null, message = e.message.toString()))
        }
    }

    fun createNewChat(user1: Int, user2: Int, lastMsg: String) = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            val result: Result<Int> = repository.addChat(user1, user2, lastMsg)
            if(result.error == null)
                emit(Resource.success(data = result.data))
            else
                emit(Resource.error(data = null, message = result.error))
        } catch (e: Exception){
            emit(Resource.error(data = null, message = e.message.toString()))
        }
    }
    
}