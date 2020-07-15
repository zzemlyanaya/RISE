package ru.citadel.rise.main.ui.projects

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ru.avangard.rise.R
import ru.avangard.rise.databinding.FragmentProjectListBinding
import ru.citadel.rise.Constants.LIST_TYPE
import ru.citadel.rise.Status
import ru.citadel.rise.data.model.Project
import ru.citadel.rise.data.model.Resource
import ru.citadel.rise.main.MainActivity

/**
 * A fragment representing a list of Items.
 */
class ProjectListFragment : Fragment() {

    private val viewModel
            by lazy { ViewModelProviders.of(this).get(ProjectListViewModel::class.java) }

    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar

    private var type: Int = 0 //0 = all, 1 = my, 2 = favourites

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        type = arguments?.getInt(LIST_TYPE) ?: 0
        val binding: FragmentProjectListBinding
                = DataBindingUtil.inflate(inflater, R.layout.fragment_project_list, container, false)
        binding.lifecycleOwner = viewLifecycleOwner

        progressBar = binding.listProgress
        recyclerView = binding.projectList

        if(type == 1) {
            binding.butAddProject.visibility = View.VISIBLE
        } else{
            binding.butAddProject.visibility = View.GONE
            val newLayoutParams =
                recyclerView.layoutParams as ConstraintLayout.LayoutParams
            newLayoutParams.topMargin = 24
            newLayoutParams.leftMargin = 16
            newLayoutParams.rightMargin = 16
            newLayoutParams.bottomMargin = 16
            recyclerView.layoutParams = newLayoutParams
        }

        with(binding.projectList) {
            layoutManager = LinearLayoutManager(context)
            adapter = ProjectRecyclerViewAdapter( {project -> showDetails(project) }, emptyList())
        }

        return binding.root
    }

    private fun showDetails(project: Project){
        (activity as MainActivity).showProjectFragment(project, type)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val id = (activity as MainActivity).currentUser.id
        when (type) {
            0 -> viewModel.fetchAllData().observe(viewLifecycleOwner, Observer { showData(it) })
            1 -> viewModel.fetchMyData(id).observe(viewLifecycleOwner, Observer { showData(it) })
            else -> viewModel.fetchFavouriteData(id).observe(viewLifecycleOwner, Observer { showData(it) })
        }
    }

    private fun showData(it: Resource<List<Project>>?){
        it?.let { resource ->
            when (resource.status) {
                Status.SUCCESS -> {
                    recyclerView.visibility = View.VISIBLE
                    progressBar.visibility = View.GONE
                    resource.data?.let { list ->
                        recyclerView.adapter =
                            ProjectRecyclerViewAdapter(
                                { project -> showDetails(project) },
                                list
                            )
                    }
                }
                Status.ERROR -> {
                    progressBar.visibility = View.INVISIBLE
                    recyclerView.visibility = View.VISIBLE
                    Toast.makeText(context, it.message, Toast.LENGTH_LONG).show()
                }
                Status.LOADING -> {
                    progressBar.visibility = View.VISIBLE
                    recyclerView.visibility = View.INVISIBLE
                }
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(type: Int): ProjectListFragment {
            val args = Bundle().apply { putInt(LIST_TYPE, type) }
            val fragment = ProjectListFragment()
            fragment.arguments = args
            return fragment
        }
    }
}