package ru.citadel.rise.main.ui.addeditproject

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.skyhope.materialtagview.model.TagModel
import ru.avangard.rise.R
import ru.avangard.rise.databinding.FragmentAddEditProjectBinding
import ru.citadel.rise.Constants.PROJECT
import ru.citadel.rise.Constants.PROJECTS_ALL
import ru.citadel.rise.Status
import ru.citadel.rise.data.local.LocalDatabase
import ru.citadel.rise.data.local.LocalRepository
import ru.citadel.rise.data.model.Project
import ru.citadel.rise.data.model.Resource
import ru.citadel.rise.main.AddEditProjectViewModelFactory
import ru.citadel.rise.main.MainActivity

class AddEditProjectFragment : Fragment() {

    private lateinit var viewModel: AddEditProjectViewModel
    private var project: Project? = null
    private var userId = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentAddEditProjectBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_add_edit_project, container, false)

        arguments?.let {
            project = it.getSerializable(PROJECT) as Project?
        }

        val activity = requireActivity() as MainActivity
        userId = activity.currentUser.userId
        val userName = activity.currentUser.name

        val dao = LocalDatabase.getDatabase(requireContext())!!.dao()
        viewModel = ViewModelProviders
            .of(this, AddEditProjectViewModelFactory(LocalRepository(dao), userId))
            .get(AddEditProjectViewModel::class.java)


        if(project != null) {
            binding.textProjName.setText(project!!.name)
            binding.textProjAbout.setText(project!!.descriptionLong)
            binding.textProjCost.setText(project!!.cost)
            binding.textProjTime.setText(project!!.deadlines)
            binding.textProjWebsite.setText(project!!.website)
            binding.tagTextView.setTagList(project!!.tags?.split(',') ?: emptyList())
        } else {
            binding.tagTextView.setTagList(ArrayList<String>())
        }

        binding.butCancel.setOnClickListener { activity.onBackPressed() }
        binding.butSaveProj.setOnClickListener {
            val proj = Project(project?.projectId ?: 0,
                binding.textProjName.text.toString(),
                userId,
                userName,
                binding.textProjAbout.text.toString(),
                binding.textProjCost.text.toString(),
                binding.textProjTime.text.toString(),
                binding.textProjWebsite.text.toString(),
                tagsToString(binding.tagTextView.selectedTags)
            )
            viewModel.addProject(proj).observe(viewLifecycleOwner, Observer { showStatus(it, proj) })
        }

        binding.butDeleteProj.setOnClickListener {
            if (project == null)
                return@setOnClickListener
            viewModel.deleteProject(project!!).observe(viewLifecycleOwner, Observer { showStatus(it, null) })
        }

        return binding.root
    }

    private fun showStatus(it: Resource<String?>, proj: Project?) {
        when(it.status){
            Status.LOADING -> {
                Toast.makeText(context, "Выполняем...", Toast.LENGTH_SHORT).show()
            }
            Status.ERROR -> {
                Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
                Log.d("SERVER", it.message.toString())
            }
            Status.SUCCESS -> {
                Toast.makeText(context, "Успешно!", Toast.LENGTH_SHORT).show()
                if (proj != null)
                    (requireActivity() as MainActivity).showProjectFragment(proj, PROJECTS_ALL)
                else
                    (requireActivity() as MainActivity).showProjectsFragment(PROJECTS_ALL, userId)
            }
        }
    }

    private fun tagsToString(tags: List<TagModel>): String {
        val res = ArrayList<String>()
        for(i in tags)
            res.add(i.tagText)
        return res.joinToString(",")
    }

    companion object {
        @JvmStatic
        fun newInstance(project: Project?): AddEditProjectFragment {
            val args = Bundle().apply {
                putSerializable(PROJECT, project)
            }
            return AddEditProjectFragment().apply {
                arguments = args
            }
        }
    }

}