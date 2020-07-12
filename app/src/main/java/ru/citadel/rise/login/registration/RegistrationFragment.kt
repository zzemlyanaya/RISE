package ru.citadel.rise.login.registration

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import ru.avangard.rise.R
import ru.avangard.rise.databinding.RegistrationFragmentBinding

class RegistrationFragment : Fragment() {

    private var onCreateAccountListener: IOnCreateAccountListener? = null

    private val viewModel by lazy { ViewModelProviders.of(this).get(RegistrationViewModel::class.java) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: RegistrationFragmentBinding = DataBindingUtil.inflate(
                inflater, R.layout.registration_fragment, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewmodel = viewModel

        binding.butRegistr.setOnClickListener { onCreateAccountListener?.onCreateNew() }
        binding.butBackToFirst.setOnClickListener { activity?.onBackPressed() }
        return binding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if(context is IOnCreateAccountListener)
            onCreateAccountListener = context
        else
            throw Exception("Must implement IOnCreateNewListener!")
    }

    override fun onDetach() {
        super.onDetach()
        onCreateAccountListener = null
    }
}

interface IOnCreateAccountListener{
    fun onCreateNew()
}