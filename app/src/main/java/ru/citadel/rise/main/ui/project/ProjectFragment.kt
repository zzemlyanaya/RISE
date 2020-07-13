package ru.citadel.rise.main.ui.project

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import ru.avangard.rise.R
import ru.avangard.rise.databinding.FragmentProjectBinding
import ru.citadel.rise.Constants.PROJECT
import ru.citadel.rise.IOnBack
import ru.citadel.rise.data.model.Project

/**
 * A simple [Fragment] subclass.
 * Use the [ProjectFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ProjectFragment : Fragment(), IOnBack {

    // TODO: Rename and change types of parameters
    private lateinit var project: Project

    override fun onBackPressed(): Boolean {
        return true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            project = it.getSerializable(PROJECT) as Project
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentProjectBinding
                    = DataBindingUtil.inflate(inflater, R.layout.fragment_project, container, false)

        binding.projTitleLong.text = project.name
        binding.projCost.text = project.cost
        binding.projTime.text = project.deadlines
        binding.projDescrLong.text = project.descriptionLong

        return binding.root
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param project Parameter 1.
         * @return A new instance of fragment ProjectFragment.
         */
        @JvmStatic
        fun newInstance(project: Project) =
            ProjectFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(PROJECT, project)
                }
            }
    }
}