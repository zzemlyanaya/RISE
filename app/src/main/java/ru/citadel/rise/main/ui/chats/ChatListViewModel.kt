package ru.citadel.rise.main.ui.chats

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.citadel.rise.data.local.LocalRepository
import ru.citadel.rise.data.model.ChatShortView
import ru.citadel.rise.data.model.Resource
import ru.citadel.rise.data.model.Result
import ru.citadel.rise.data.remote.RemoteRepository

class ChatListViewModel(private val localRepository: LocalRepository,
                        private val curUserId: Int) : ViewModel() {
    private val remoteRepository = RemoteRepository()

    var chats = MutableLiveData<List<ChatShortView>>()

    init {
        CoroutineScope(Dispatchers.IO).launch {
            chats.postValue(localRepository.getAllUserChats(curUserId))
        }
    }

    fun fetchAllChatsRemotely() = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            val result: Result<List<ChatShortView>> = remoteRepository.getChatsByUser(curUserId)
            if (result.error == null) {
                emit(Resource.success(data = result.data))
                localRepository.insertChat(result.data!!)
                chats.postValue(result.data)
            }
            else
                emit(Resource.error(data = null, message = result.error))
        } catch (e: Exception){
            emit(Resource.error(data = null, message = e.message.toString()))
        }
    }
}