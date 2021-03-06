package ru.citadel.rise.main.ui.projects

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.textview.MaterialTextView
import ru.citadel.rise.Constants.LIST_TYPE
import ru.citadel.rise.Constants.PROJECTS_ALL
import ru.citadel.rise.Constants.PROJECTS_BY_USER
import ru.citadel.rise.Constants.PROJECTS_MY
import ru.citadel.rise.Constants.SHOW_CANT_GET_LOCALLY
import ru.citadel.rise.Constants.SHOW_CANT_GET_REMOTELY
import ru.citadel.rise.Constants.SHOW_CANT_UPDATE
import ru.citadel.rise.Constants.USER
import ru.citadel.rise.R
import ru.citadel.rise.Status
import ru.citadel.rise.data.local.LocalDatabase
import ru.citadel.rise.data.local.LocalRepository
import ru.citadel.rise.data.model.Project
import ru.citadel.rise.data.model.Resource
import ru.citadel.rise.databinding.FragmentProjectListBinding
import ru.citadel.rise.main.MainActivity
import ru.citadel.rise.main.ProjectListViewModelFactory

/**
 * A fragment representing a list of Items.
 */
class ProjectListFragment : Fragment() {

    private lateinit var viewModel: ProjectListViewModel

    private lateinit var recyclerView: RecyclerView
    private lateinit var refreshLayout: SwipeRefreshLayout
    private lateinit var textConnect: MaterialTextView

    private var type: Int = PROJECTS_ALL
    private var userId: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        type = arguments?.getInt(LIST_TYPE) ?: PROJECTS_ALL
        userId = arguments?.getInt(USER) ?: 0

        val binding: FragmentProjectListBinding
                = DataBindingUtil.inflate(inflater, R.layout.fragment_project_list, container, false)
        binding.lifecycleOwner = viewLifecycleOwner


        val dao = LocalDatabase.getDatabase(requireContext())!!.dao()
        viewModel = ViewModelProviders
            .of(this, ProjectListViewModelFactory(LocalRepository.getInstance(dao)))
            .get(ProjectListViewModel::class.java)

        recyclerView = binding.projectList
        refreshLayout = binding.projectListRefreshLayout
        textConnect = binding.textConnect

        with(binding.projectList) {
            layoutManager = LinearLayoutManager(context)
            adapter = ProjectRecyclerViewAdapter({ project -> showDetails(project) }, emptyList())
        }

        refreshLayout.setOnRefreshListener {
            getData()
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        when (type) {
            PROJECTS_ALL -> {
                viewModel.fetchAllProjectsLocally().observe(viewLifecycleOwner, { showData(it, SHOW_CANT_GET_LOCALLY) })
                viewModel.fetchAllProjects().observe(viewLifecycleOwner, { showData(it, SHOW_CANT_UPDATE) })
            }
            PROJECTS_MY -> viewModel.fetchMyProjectsLocally(userId).observe(viewLifecycleOwner,
                { showData(it, SHOW_CANT_GET_LOCALLY) })
            PROJECTS_BY_USER -> viewModel.fetchProjectsByUser(userId).observe(viewLifecycleOwner,
                { showData(it, SHOW_CANT_UPDATE) })
            else -> viewModel.fetchFavProjectsLocally(userId).observe(viewLifecycleOwner,
                { showData(it, SHOW_CANT_GET_LOCALLY) })
        }
    }

    private fun showDetails(project: Project){
        (activity as MainActivity).showProjectFragment(project, type)
    }


    private fun getData() {
        when (type) {
            PROJECTS_ALL -> viewModel.fetchAllProjects().observe(viewLifecycleOwner,
                { showData(it, SHOW_CANT_UPDATE) })
            PROJECTS_MY -> viewModel.fetchProjectsByUser(userId).observe(viewLifecycleOwner, {
                showData(it, SHOW_CANT_GET_LOCALLY)
                viewModel.updateMyProjects(userId, it.data ?: emptyList())
            })
            PROJECTS_BY_USER -> viewModel.fetchProjectsByUser(userId)
            else -> viewModel.fetchFavProjectsLocally(userId)
        }
    }

    private fun showData(resource: Resource<List<Project>?>, errorOption: Int){
        when (resource.status) {
            Status.SUCCESS -> {
                recyclerView.visibility = View.VISIBLE
                textConnect.visibility = View.INVISIBLE
                refreshLayout.isRefreshing = false
                resource.data?.let { list ->
                    recyclerView.adapter =
                        ProjectRecyclerViewAdapter(
                            { project -> showDetails(project) },
                            list
                        )
                }
            }
            Status.ERROR -> {
                recyclerView.visibility = View.VISIBLE
                refreshLayout.isRefreshing = false
                if (errorOption == SHOW_CANT_UPDATE && !viewModel.isUpdated)
                    AlertDialog.Builder(requireContext())
                        .setMessage(R.string.cant_update)
                        .setPositiveButton("OK") { dialog, _ -> run { dialog.cancel() } }
                        .create()
                        .show()
                else if (errorOption == SHOW_CANT_GET_REMOTELY)
                    AlertDialog.Builder(requireContext())
                        .setMessage(R.string.connection_failed)
                        .setPositiveButton("OK") { dialog, _ -> run { dialog.cancel() } }
                        .create()
                        .show()
                else
                    textConnect.visibility = View.VISIBLE
            }
            Status.LOADING -> {
                recyclerView.visibility = View.INVISIBLE
                textConnect.visibility = View.INVISIBLE
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(type: Int, userId: Int): ProjectListFragment {
            val args = Bundle().apply {
                putInt(LIST_TYPE, type)
                putInt(USER, userId)
            }
            val fragment = ProjectListFragment()
            fragment.arguments = args
            return fragment
        }
    }
}