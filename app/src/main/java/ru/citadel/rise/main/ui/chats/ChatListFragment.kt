package ru.citadel.rise.main.ui.chats

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textview.MaterialTextView
import ru.avangard.rise.R
import ru.avangard.rise.databinding.FragmentChatListBinding
import ru.citadel.rise.Status
import ru.citadel.rise.data.model.Resource
import ru.citadel.rise.main.MainActivity
import ru.citadel.rise.main.ui.chat.UserShortView
import ru.citadel.rise.toInt
import kotlin.random.Random

/**
 * A fragment representing a list of Items.
 */
class ChatListFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var textConnect: MaterialTextView

    private var userId = 0

    private val viewModel
            by lazy { ViewModelProviders.of(this).get(ChatListViewModel::class.java)}

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        userId = (activity as MainActivity).currentUser.id

        val binding: FragmentChatListBinding
                = DataBindingUtil.inflate(inflater, R.layout.fragment_chat_list, container, false)

        with(binding.chatList) {
            layoutManager = LinearLayoutManager(context)
            adapter = ChatsRecyclerViewAdapter({onChatClick(it)}, emptyList())
        }

        recyclerView = binding.chatList
        textConnect = binding.textConnect
        progressBar = binding.listProgress

        textConnect.setOnClickListener { getData() }
        return binding.root
    }

    private fun onChatClick(it: ChatShortView){
        (activity as MainActivity).showChatFragment(
            UserShortView(it.toID, it.toName, (Random.nextInt() % 2 == 0).toInt()),
            it.chat_id
        )
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel.fetchAllChats(userId).observe(viewLifecycleOwner, Observer { showData(it) })
    }

    private fun getData(){
        viewModel.fetchAllChats(userId)
    }

    private fun showData(it: Resource<List<ChatShortView>?>){
        it.let { resource ->
            when (resource.status) {
                Status.SUCCESS -> {
                    recyclerView.visibility = View.VISIBLE
                    progressBar.visibility = View.GONE
                    resource.data?.let { list ->
                        recyclerView.adapter =
                            ChatsRecyclerViewAdapter({onChatClick(it)}, list)
                    }
                }
                Status.ERROR -> {
                    progressBar.visibility = View.INVISIBLE
                    recyclerView.visibility = View.VISIBLE
                    textConnect.visibility = View.VISIBLE
                }
                Status.LOADING -> {
                    progressBar.visibility = View.VISIBLE
                    recyclerView.visibility = View.INVISIBLE
                    textConnect.visibility = View.INVISIBLE
                }
            }
        }
    }
}