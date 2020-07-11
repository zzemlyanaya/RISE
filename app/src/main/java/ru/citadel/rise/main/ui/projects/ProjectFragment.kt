package ru.citadel.rise.main.ui.projects

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import ru.avangard.rise.R
import ru.avangard.rise.databinding.FragmentProjectListBinding
import ru.citadel.rise.model.Project

/**
 * A fragment representing a list of Items.
 */
class ProjectFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentProjectListBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_project_list, container, false)

        with(binding.projectList) {
            layoutManager = LinearLayoutManager(context)
            adapter = ProjectRecyclerViewAdapter(listOf(
                Project("", "RISE", listOf("8937"), "The best app ever",
                    "10000 рублей", "1 месяц"
                )
            ))
        }

        return binding.root
    }

}