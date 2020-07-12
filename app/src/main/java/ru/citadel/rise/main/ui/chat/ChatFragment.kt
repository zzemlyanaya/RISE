package ru.citadel.rise.main.ui.chat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import ru.avangard.rise.R
import ru.avangard.rise.databinding.ChatFragmentBinding
import ru.citadel.rise.IOnBack

class ChatFragment : Fragment(), IOnBack {

    private val viewModel by lazy { ViewModelProviders.of(this).get(ChatViewModel::class.java) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: ChatFragmentBinding
                = DataBindingUtil.inflate(inflater, R.layout.chat_fragment, container, false)
        binding.lifecycleOwner = viewLifecycleOwner

        return binding.root
    }

    override fun onBackPressed(): Boolean {
        return true
    }

}