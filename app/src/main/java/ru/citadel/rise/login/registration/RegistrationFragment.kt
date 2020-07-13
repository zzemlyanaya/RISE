package ru.citadel.rise.login.registration

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import ru.avangard.rise.R
import ru.avangard.rise.databinding.RegistrationFragmentBinding
import ru.citadel.rise.afterTextChanged
import ru.citadel.rise.data.model.User
import ru.citadel.rise.toInt

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

        binding.butRegistr.setOnClickListener {
            onCreateAccountListener?.onCreateNew(
                User(
                    binding.textEmail.text.hashCode().toString(),
                    binding.textName.text.toString(),
                    viewModel.isPersonChecked.value?.toInt()!!,
                    null, null, null, null, null
                )
            )
        }
        binding.butBackToFirst.setOnClickListener { activity?.onBackPressed() }

        binding.textEmail.afterTextChanged {
            viewModel.loginDataChanged(
                binding.textEmail.text.toString(),
                binding.textName.text.toString(),
                binding.textPassword.text.toString()
            )
        }
        binding.textName.afterTextChanged {
            viewModel.loginDataChanged(
                binding.textEmail.text.toString(),
                binding.textName.text.toString(),
                binding.textPassword.text.toString()
            )
        }
        binding.textPassword.apply {
            afterTextChanged {
                viewModel.loginDataChanged(
                    binding.textEmail.text.toString(),
                    binding.textName.text.toString(),
                    binding.textPassword.text.toString()
                )
            }

            setOnEditorActionListener { _, actionId, _ ->
                when (actionId) {
                    EditorInfo.IME_ACTION_DONE ->
                        onCreateAccountListener?.onCreateNew(
                            User(
                                binding.textEmail.text.hashCode().toString(),
                                binding.textName.text.toString(),
                                viewModel.isPersonChecked.value?.toInt()!!,
                                null, null, null, null, null
                            )
                        )
                }
                false
            }

        }

        viewModel.registrationFormState.observe(viewLifecycleOwner, Observer {
            val state = it ?: return@Observer

            // disable login button unless both username / password is valid
            binding.butRegistr.isEnabled = state.isDataValid

            binding.inputEmail.error = getString(state.emailError)
            binding.inputName.error = getString(state.nameError)
            binding.inputPassword.error = getString(state.passwordError)
        })

        return binding.root
    }

    private fun getString(id: Int?): String? {
        return if (id == null) null else getString(id)
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
    fun onCreateNew(user: User)
}