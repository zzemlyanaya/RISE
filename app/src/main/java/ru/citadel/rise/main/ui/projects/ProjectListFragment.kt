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
import ru.citadel.rise.main.MainActivity
import ru.citadel.rise.model.Project

/**
 * A fragment representing a list of Items.
 */
class ProjectListFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentProjectListBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_project_list, container, false)

        with(binding.projectList) {
            layoutManager = LinearLayoutManager(context)
            adapter = ProjectRecyclerViewAdapter {
                (activity as MainActivity).showProjectFragment(it)
            }
            (adapter as ProjectRecyclerViewAdapter).setData(
                listOf(
                    Project("", "RISE", listOf("8937"), "The best startup platform ever",
                        "Some very long text which i definitely don't won't to type so it's kinda short text",
                        "1000000 рублей", "1 месяц"
                    ),
                    Project("", "CITADEL Education", listOf("435"), "The best education platform ever",
                        "Some very long text which i definitely don't won't to type so it's kinda short text",
                        "1100000 рублей", "2 месяца"
                    )
                )
            )
        }

        return binding.root
    }

}