package ru.citadel.rise.main.ui.chats

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import ru.avangard.rise.R
import ru.avangard.rise.databinding.FragmentChatListBinding
import ru.citadel.rise.data.model.User
import ru.citadel.rise.main.MainActivity

/**
 * A fragment representing a list of Items.
 */
class ChatListFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentChatListBinding
                = DataBindingUtil.inflate(inflater, R.layout.fragment_chat_list, container, false)

        with(binding.chatList) {
            layoutManager = LinearLayoutManager(context)
            adapter = ChatsRecyclerViewAdapter{
                (activity as MainActivity).showChatFragment(
                    User("awd", it.from, 1, 27, null, null, null, null)
                )
            }
            (adapter as ChatsRecyclerViewAdapter).setData((listOf(
                ChatShortView("Person #1", "sdf", "hi there", "00:00"),
                ChatShortView("Person #2", "sdf", "general", "00:01"),
                ChatShortView("Person #3", "sdf", "kenobi", "00:02")
            )))
        }
        return binding.root
    }
}