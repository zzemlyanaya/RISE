package ru.citadel.rise.main.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import ru.avangard.rise.R
import ru.avangard.rise.databinding.FragmentProfileBinding
import ru.citadel.rise.main.MainActivity

// TODO: Rename parameter arguments, choose names that match


/**
 * A simple [Fragment] subclass.
 * Use the [ProfileFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ProfileFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentProfileBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false)

        binding.butSettings.setOnClickListener { (activity as MainActivity).showSettingsFragment() }
        binding.butFavourites.setOnClickListener { (activity as MainActivity).showProjectsFragment(2) }
        binding.butMyProjects.setOnClickListener { (activity as MainActivity).showProjectsFragment(1) }

        val user = (activity as MainActivity).currentUser

        binding.profileName.text = user.name
        when(user.type){
            1 -> binding.profileImage.setImageResource(R.drawable.im_robot_with_idea)
            else -> binding.profileImage.setImageResource(R.drawable.im_robot_manager)
        }

        return binding.root
    }
}