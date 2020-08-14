package ru.citadel.rise.main.ui.projects

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textview.MaterialTextView
import ru.avangard.rise.R
import ru.avangard.rise.databinding.FragmentProjectListBinding
import ru.citadel.rise.Constants.LIST_TYPE
import ru.citadel.rise.Constants.PROJECTS_ALL
import ru.citadel.rise.Constants.PROJECTS_MY
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
    private lateinit var textConnect: MaterialTextView

    private var type: Int = PROJECTS_ALL
    private var userId: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        type = arguments?.getInt(LIST_TYPE) ?: PROJECTS_ALL
        val binding: FragmentProjectListBinding
                = DataBindingUtil.inflate(inflater, R.layout.fragment_project_list, container, false)
        binding.lifecycleOwner = viewLifecycleOwner

        progressBar = binding.listProgress
        recyclerView = binding.projectList
        textConnect = binding.textConnect
        textConnect.setOnClickListener { getData() }

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
        userId = (activity as MainActivity).currentUser.userId
        when (type) {
            PROJECTS_ALL -> viewModel.fetchAllData().observe(viewLifecycleOwner, Observer { showData(it) })
            PROJECTS_MY -> viewModel.fetchMyData(userId).observe(viewLifecycleOwner, Observer { showData(it) })
            else -> viewModel.fetchFavouriteData(userId).observe(viewLifecycleOwner, Observer { showData(it) })
        }
    }

    private fun getData() {
//        viewModel.fetchAllData().removeObservers(viewLifecycleOwner)
//        viewModel.fetchFavouriteData(id).removeObservers(viewLifecycleOwner)
//        viewModel.fetchMyData(id).removeObservers(viewLifecycleOwner)
        when (type) {
            PROJECTS_ALL -> viewModel.fetchAllData()
            PROJECTS_MY -> viewModel.fetchMyData(userId)
            else -> viewModel.fetchFavouriteData(userId)
        }
    }

    private fun showData(it: Resource<List<Project>?>){
        it.let { resource ->
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
                    textConnect.visibility = View.VISIBLE
                }
                Status.LOADING -> {
                    progressBar.visibility = View.VISIBLE
                    recyclerView.visibility = View.INVISIBLE
                    textConnect.visibility = View.INVISIBLE
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