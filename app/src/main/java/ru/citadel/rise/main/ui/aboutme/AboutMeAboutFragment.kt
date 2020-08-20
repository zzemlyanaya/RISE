package ru.citadel.rise.main.ui.aboutme

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import ru.citadel.rise.Constants.USER
import ru.citadel.rise.R
import ru.citadel.rise.data.model.User
import ru.citadel.rise.databinding.FragmentAboutMeAboutBinding


class AboutMeAboutFragment : Fragment() {

    private lateinit var user: User

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentAboutMeAboutBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_about_me_about, container, false)

        user = requireArguments().getBundle(USER)!!.getSerializable(USER) as User

        if (user.city != null && user.country != null)
           binding.aboutMeLocation.text = "${user.city}, ${user.country}"
        if (user.city == null && user.country != null)
            binding.aboutMeLocation.text = "${user.country}"

        binding.aboutMeText.text = user.about ?: resources.getString(R.string.empty_about)

        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance(user: User): AboutMeAboutFragment {
            val args = Bundle().apply {
                val bundle = Bundle().apply { putSerializable(USER, user) }
                putBundle(USER, bundle)
            }
            val fragment = AboutMeAboutFragment()
            fragment.arguments = args
            return fragment
        }
    }
}