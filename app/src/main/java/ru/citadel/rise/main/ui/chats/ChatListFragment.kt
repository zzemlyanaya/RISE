package ru.citadel.rise.main.ui.chats

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import ru.citadel.rise.R
import ru.citadel.rise.Status
import ru.citadel.rise.data.local.LocalDatabase
import ru.citadel.rise.data.local.LocalRepository
import ru.citadel.rise.data.model.ChatShortView
import ru.citadel.rise.data.model.Resource
import ru.citadel.rise.databinding.FragmentChatListBinding
import ru.citadel.rise.main.ChatListViewModelFactory
import ru.citadel.rise.main.MainActivity
import ru.citadel.rise.main.ui.chat.UserShortView

/**
 * A fragment representing a list of Items.
 */
class ChatListFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var refreshLayout: SwipeRefreshLayout

    private var userId = 0

    private lateinit var viewModel: ChatListViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        userId = (activity as MainActivity).currentUser.userId

        val dao = LocalDatabase.getDatabase(requireContext())!!.dao()
        viewModel = ViewModelProviders
            .of(this, ChatListViewModelFactory(LocalRepository.getInstance(dao), userId))
            .get(ChatListViewModel::class.java)

        val binding: FragmentChatListBinding
                = DataBindingUtil.inflate(inflater, R.layout.fragment_chat_list, container, false)

        with(binding.chatList) {
            layoutManager = LinearLayoutManager(context)
            adapter = ChatsRecyclerViewAdapter({onChatClick(it)}, emptyList())
        }

        recyclerView = binding.chatList

        refreshLayout = binding.refreshLayout
        refreshLayout.setOnRefreshListener {
            refreshData()
        }

        return binding.root
    }

    private fun onChatClick(it: ChatShortView){
        (activity as MainActivity).showChatFragment(
            UserShortView(it.toId, it.toName),
            it.chatId
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.chats.observe(viewLifecycleOwner, { showData(Resource.success(it)) })
    }

    private fun refreshData(){
        viewModel.fetchAllChatsRemotely().observe(viewLifecycleOwner, { showData(it) })
    }

    private fun showData(it: Resource<List<ChatShortView>?>){
        when (it.status) {
            Status.SUCCESS -> {
                refreshLayout.isRefreshing = false
                recyclerView.visibility = View.VISIBLE
                it.data?.let { list ->
                    recyclerView.adapter =
                        ChatsRecyclerViewAdapter({onChatClick(it)}, list)
                }
            }
            Status.ERROR -> {
                refreshLayout.isRefreshing = false
                recyclerView.visibility = View.VISIBLE
                Toast.makeText(requireContext(), getString(R.string.connection_on_list), Toast.LENGTH_SHORT).show()
            }
            Status.LOADING -> {
                recyclerView.visibility = View.INVISIBLE
            }
        }
    }
}