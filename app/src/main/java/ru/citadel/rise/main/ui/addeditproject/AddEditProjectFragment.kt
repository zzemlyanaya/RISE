package ru.citadel.rise.main.ui.addeditproject

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import ru.avangard.rise.R
import ru.avangard.rise.databinding.FragmentAddEditProjectBinding
import ru.citadel.rise.main.MainActivity

class AddEditProjectFragment : Fragment() {

    private lateinit var viewModel: AddEditProjectViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentAddEditProjectBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_add_edit_project, container, false)

        binding.butCancel.setOnClickListener { (activity as MainActivity).onBackPressed() }

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(AddEditProjectViewModel::class.java)
    }

}