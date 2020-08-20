package ru.citadel.rise.main.ui.project

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.google.android.material.chip.Chip
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.citadel.rise.Constants.PROJECT
import ru.citadel.rise.Constants.USER
import ru.citadel.rise.IOnBack
import ru.citadel.rise.R
import ru.citadel.rise.data.local.LocalDatabase
import ru.citadel.rise.data.local.LocalRepository
import ru.citadel.rise.data.model.Project
import ru.citadel.rise.databinding.FragmentProjectBinding
import ru.citadel.rise.main.MainActivity
import ru.citadel.rise.main.ui.chat.UserShortView

/**
 * A simple [Fragment] subclass.
 * Use the [ProjectFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ProjectFragment : Fragment(), IOnBack {

    private lateinit var project: Project
    private var curUserId = 0

    private lateinit var localRepository: LocalRepository

    override fun onBackPressed(): Boolean {
        return true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            project = it.getSerializable(PROJECT) as Project
            curUserId = it.getInt(USER)
        }

        val dao = LocalDatabase.getDatabase(requireContext())!!.dao()
        localRepository = LocalRepository.getInstance(dao)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentProjectBinding
                    = DataBindingUtil.inflate(inflater, R.layout.fragment_project, container, false)

        binding.projCost.text = project.cost ?: resources.getString(R.string.empty_proj)
        binding.projTime.text = project.deadlines ?: resources.getString(R.string.empty_proj)
        binding.projWebsite.text = project.website ?: resources.getString(R.string.empty_proj)
        binding.projDescrLong.text = project.descriptionLong

        val tags = project.tags?.split(',') ?: emptyList()
        for (i in tags){
            val chip = Chip(binding.tagsGroup.context)
            chip.text = i
            binding.tagsGroup.addView(chip)
        }

        if (project.contact == curUserId) {
            binding.butContact.apply {
                text = getString(R.string.edit)
                setOnClickListener {
                    (requireActivity() as MainActivity).showAddEditProject(project)
                }
            }
        } else {
            binding.butContact.apply {
                text = getString(R.string.but_contact)
                setOnClickListener {
                    contact()
                    binding.projProgress.visibility = View.VISIBLE
                }
            }
        }

        return binding.root
    }

    private fun contact() = run {
        CoroutineScope(Dispatchers.IO).launch {
            val id = localRepository.getChatIdByUsers(curUserId, project.contact)
            val shortView = UserShortView(project.contact, project.contactName)
            withContext(Dispatchers.Main) {
                (requireActivity() as MainActivity).showChatFragment(shortView, id ?: 0)
            }
        }

    }

    companion object {
        @JvmStatic
        fun newInstance(project: Project, id: Int) =
            ProjectFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(PROJECT, project)
                    putInt(USER, id)
                }
            }
    }
}