package ru.citadel.rise.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ru.citadel.rise.data.local.LocalRepository
import ru.citadel.rise.data.model.User
import ru.citadel.rise.main.ui.addeditproject.AddEditProjectViewModel
import ru.citadel.rise.main.ui.chat.ChatViewModel
import ru.citadel.rise.main.ui.chats.ChatListViewModel
import ru.citadel.rise.main.ui.projects.ProjectListViewModel

class MainViewModelFactory(private val repo: LocalRepository, val user: User) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return MainViewModel(repo, user) as T
    }
}

class ChatListViewModelFactory(private val repo: LocalRepository, val userId: Int) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return ChatListViewModel(repo, userId) as T
    }
}

class ProjectListViewModelFactory(private val repo: LocalRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return ProjectListViewModel(repo) as T
    }
}

class AddEditProjectViewModelFactory(private val repo: LocalRepository, val userId: Int) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return AddEditProjectViewModel(repo, userId) as T
    }
}

class ChatViewModelFactory(private val repo: LocalRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return ChatViewModel(repo) as T
    }
}