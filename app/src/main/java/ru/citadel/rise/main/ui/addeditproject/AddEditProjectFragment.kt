package ru.citadel.rise.main.ui.addeditproject

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import ru.avangard.rise.R
import ru.avangard.rise.databinding.FragmentAddEditProjectBinding
import ru.citadel.rise.Constants.PROJECTS_ALL
import ru.citadel.rise.Status
import ru.citadel.rise.data.model.Project
import ru.citadel.rise.main.MainActivity

class AddEditProjectFragment : Fragment() {

    private val viewModel by lazy { ViewModelProviders.of(this).get(AddEditProjectViewModel::class.java)  }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentAddEditProjectBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_add_edit_project, container, false)

        val activity = requireActivity() as MainActivity

        binding.butCancel.setOnClickListener { activity.onBackPressed() }
        binding.butSaveProj.setOnClickListener {
            val proj = Project(0,
                binding.textProjName.text.toString(),
                activity.currentUser.userId,
                activity.currentUser.name,
                binding.textProjAbout.text.toString(),
                binding.textProjCost.text.toString(),
                binding.textProjTime.text.toString(),
                binding.textProjWebsite.text.toString(),
                binding.tagTextView.selectedTags.joinToString(",")
            )
            viewModel.addProject(proj).observe(viewLifecycleOwner, Observer {
                when(it.status){
                    Status.LOADING -> {
                        Toast.makeText(context, "Создаём...", Toast.LENGTH_SHORT).show()
                    }
                    Status.ERROR -> {
                        Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
                    }
                    Status.SUCCESS -> {
                        Toast.makeText(context, "Успешно!", Toast.LENGTH_SHORT).show()
                        activity.showProjectsFragment(PROJECTS_ALL)
                    }
                }
            })
        }
        binding.tagTextView.setTagList(ArrayList<String>())

        return binding.root
    }


}