package ru.citadel.rise.main.ui.aboutme

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import ru.avangard.rise.R
import ru.avangard.rise.databinding.FragmentAboutMeAboutBinding
import ru.citadel.rise.main.MainActivity


class AboutMeAboutFragment : Fragment() {

    private val user by lazy { (activity as MainActivity).currentUser }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentAboutMeAboutBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_about_me_about, container, false)


        if (user.city != null && user.country != null)
           binding.aboutMeLocation.text = "${user.city}, ${user.country}"
        if (user.city == null && user.country != null)
            binding.aboutMeLocation.text = "${user.country}"

        binding.aboutMeAge.text = user.age?.toString() ?: resources.getString(R.string.empty_about)

        binding.aboutMeText.text = user.about ?: resources.getString(R.string.empty_about)

        return binding.root
    }
}