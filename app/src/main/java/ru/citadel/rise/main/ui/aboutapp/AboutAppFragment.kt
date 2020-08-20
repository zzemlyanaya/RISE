package ru.citadel.rise.main.ui.aboutapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import ru.citadel.rise.R
import ru.citadel.rise.databinding.FragmentAboutAppBinding
import ru.citadel.rise.main.MainActivity


class AboutAppFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentAboutAppBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_about_app, container, false)

        binding.butBack.setOnClickListener { (activity as MainActivity).onBackPressed() }
        return binding.root
    }

}