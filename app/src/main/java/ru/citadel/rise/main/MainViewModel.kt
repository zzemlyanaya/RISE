package ru.citadel.rise.main

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.citadel.rise.data.local.LocalRepository
import ru.citadel.rise.data.model.ChatShortView
import ru.citadel.rise.data.model.User
import ru.citadel.rise.data.model.UserChatRelation
import ru.citadel.rise.data.remote.RemoteRepository

class MainViewModel(private val localRepository: LocalRepository,
                    private val user: User) : ViewModel() {
    private val remoteRepository = RemoteRepository()

    init {
        CoroutineScope(Dispatchers.IO).launch {
            localRepository.insertUser(user)
            updateChats()
        }
    }

    private fun updateChats(){
        val remoteChatsList: List<ChatShortView>? = try {
            remoteRepository.getChatsByUser(user.userId).data
        } catch (e: Exception) {
            null
        }

        if (remoteChatsList != null) {
            val localChatList = localRepository.getAllUserChats(user.userId)
            if (localChatList != remoteChatsList) {
                localRepository.insertChat(remoteChatsList)
                for (i in remoteChatsList) {
                    localRepository.insertUserChatRelation(UserChatRelation(user.userId, i.chatId))
                }
            }
        }
    }
}