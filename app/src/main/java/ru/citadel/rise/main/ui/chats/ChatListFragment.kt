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
import ru.citadel.rise.main.MainActivity
import ru.citadel.rise.main.ui.chat.UserShortView
import ru.citadel.rise.toInt
import kotlin.random.Random

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
                    UserShortView(it.toID, it.toName, (Random.nextInt() % 2 == 0).toInt())
                )
            }
            val id = (activity as MainActivity).currentUser.id
            (adapter as ChatsRecyclerViewAdapter).setData((listOf(
                ChatShortView(id, 1, "Person #1", "hi there", "00:00"),
                ChatShortView(id, 2, "Person #2", "general", "00:01"),
                ChatShortView(id, 3, "Person #3", "kenobi", "00:02")
            )))
        }
        return binding.root
    }
}