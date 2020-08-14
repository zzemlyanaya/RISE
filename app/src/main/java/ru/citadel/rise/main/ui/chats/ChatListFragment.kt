package ru.citadel.rise.main.ui.chats

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.textview.MaterialTextView
import ru.avangard.rise.R
import ru.avangard.rise.databinding.FragmentChatListBinding
import ru.citadel.rise.Status
import ru.citadel.rise.data.local.LocalDatabase
import ru.citadel.rise.data.local.LocalRepository
import ru.citadel.rise.data.model.ChatShortView
import ru.citadel.rise.data.model.Resource
import ru.citadel.rise.main.ChatListViewModelFactory
import ru.citadel.rise.main.MainActivity
import ru.citadel.rise.main.ui.chat.UserShortView

/**
 * A fragment representing a list of Items.
 */
class ChatListFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var textConnect: MaterialTextView
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
            .of(this, ChatListViewModelFactory(LocalRepository(dao), userId))
            .get(ChatListViewModel::class.java)

        val binding: FragmentChatListBinding
                = DataBindingUtil.inflate(inflater, R.layout.fragment_chat_list, container, false)

        with(binding.chatList) {
            layoutManager = LinearLayoutManager(context)
            adapter = ChatsRecyclerViewAdapter({onChatClick(it)}, emptyList())
        }

        recyclerView = binding.chatList
        textConnect = binding.textConnect

        textConnect.setOnClickListener { refreshData() }

        refreshLayout = binding.refreshLayout
        refreshLayout.setOnRefreshListener {
            refreshData()
        }

        return binding.root
    }

    private fun onChatClick(it: ChatShortView){
        (activity as MainActivity).showChatFragment(
            UserShortView(it.toID, it.toName),
            it.chatId
        )
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel.chats.observe(viewLifecycleOwner, Observer { showData(Resource.success(it)) })
    }

    private fun refreshData(){
        viewModel.fetchAllChatsRemotely().observe(viewLifecycleOwner, Observer { showData(it) })
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
                textConnect.visibility = View.VISIBLE
            }
            Status.LOADING -> {
                recyclerView.visibility = View.INVISIBLE
                textConnect.visibility = View.INVISIBLE
            }
        }
    }
}