package ru.citadel.rise.main

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.citadel.rise.data.local.LocalRepository
import ru.citadel.rise.data.model.*
import ru.citadel.rise.data.remote.RemoteRepository

class MainViewModel(private val localRepository: LocalRepository,
                    private val user: User) : ViewModel() {
    private val remoteRepository = RemoteRepository()

    private val fakeData = listOf(
        Project(1, "RISE", 1, "CITADEL",
            "The best startup platform ever",
            "1000000 рублей", "1 месяц", "http://bestApp.ever/RISE", "android,B2B,B2C,B2G,startup"),

        Project(2, "CITADEL Education", 1, "CITADEL",
            "The best education platform ever",
            "1100000 рублей", "2 месяца", "http://bestApp.ever/Education", "web,AI,neutral networks")
    )

    init {
        CoroutineScope(Dispatchers.IO).launch {
            localRepository.insertProject(fakeData)
            localRepository.insertUser(user)
            for(i in fakeData)
                localRepository.deleteUserWithTheirProject(UserWithTheirProjects(user.userId, i.projectId))
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

    fun logout() {
        remoteRepository.logout()
    }
}