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

/**
 * A fragment representing a list of Items.
 */
class ChatsFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentChatListBinding
                = DataBindingUtil.inflate(inflater, R.layout.fragment_chat_list, container, false)

        with(binding.chatList) {
            layoutManager = LinearLayoutManager(context)
            adapter = ChatsRecyclerViewAdapter(listOf(
                ChatShortView("Person #1", "ushdf", "hello there", "00:00"),
                ChatShortView("Person #2", "ushdf", "general", "00:01"),
                ChatShortView("Person #3", "ushdf", "kenobi", "00:02")
            ))
        }
        return binding.root
    }
}