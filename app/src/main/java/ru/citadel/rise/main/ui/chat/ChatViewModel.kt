package ru.citadel.rise.main.ui.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject
import ru.citadel.rise.data.local.LocalRepository
import ru.citadel.rise.data.model.ChatShortView
import ru.citadel.rise.data.model.Message
import ru.citadel.rise.data.model.Resource
import ru.citadel.rise.data.model.Result
import ru.citadel.rise.data.remote.RemoteRepository


class ChatViewModel(private val localRepository: LocalRepository, private val userId: Int) : ViewModel() {

    private val repository = RemoteRepository()

    private val curChatId = 0

    private var chatList = liveData(Dispatchers.IO){
        emit(localRepository.getAllUserChats(userId))
    }


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
            val json = JSONObject(mapOf("user1" to user1, "user2" to user2, "lastMessage" to lastMsg))
            val result: Result<ChatShortView> = repository.addChat(json)
            if(result.error == null) {
                emit(Resource.success(data = result.data))
                localRepository.insertChat(listOf(result.data!!))
            }
            else
                emit(Resource.error(data = null, message = result.error))
        } catch (e: Exception){
            emit(Resource.error(data = null, message = e.message.toString()))
        }
    }

    fun updateChatLastMessage(newChat: ChatShortView) = run {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                localRepository.updateChat(newChat)
            }
            catch (e: Exception){
            }
        }
    }
    
}