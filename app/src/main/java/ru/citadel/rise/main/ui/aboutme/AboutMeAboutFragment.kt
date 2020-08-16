package ru.citadel.rise.main.ui.aboutme

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import dev.ahmedmourad.bundlizer.Bundlizer
import ru.avangard.rise.R
import ru.avangard.rise.databinding.FragmentAboutMeAboutBinding
import ru.citadel.rise.Constants.USER
import ru.citadel.rise.data.model.User


class AboutMeAboutFragment : Fragment() {

    private lateinit var user: User

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentAboutMeAboutBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_about_me_about, container, false)

        user = Bundlizer.unbundle(User.serializer(), requireArguments().getBundle(USER)!!)

        if (user.city != null && user.country != null)
           binding.aboutMeLocation.text = "${user.city}, ${user.country}"
        if (user.city == null && user.country != null)
            binding.aboutMeLocation.text = "${user.country}"

        binding.aboutMeAge.text = user.age?.toString() ?: resources.getString(R.string.empty_about)

        binding.aboutMeText.text = user.about ?: resources.getString(R.string.empty_about)

        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance(user: User): AboutMeAboutFragment {
            val args = Bundle().apply {
                val bundle: Bundle = Bundlizer.bundle(User.serializer(), user)
                putBundle(USER, bundle)
            }
            val fragment = AboutMeAboutFragment()
            fragment.arguments = args
            return fragment
        }
    }
}