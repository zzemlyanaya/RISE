package ru.citadel.rise.login.registration

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.ProgressBar
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.button.MaterialButton
import ru.citadel.rise.R
import ru.citadel.rise.Status
import ru.citadel.rise.afterTextChanged
import ru.citadel.rise.data.model.User
import ru.citadel.rise.databinding.FragmentRegistrationBinding
import ru.citadel.rise.toInt

class RegistrationFragment : Fragment() {

    private var onCreateAccountListener: IOnCreateAccountListener? = null

    private lateinit var progressBar: ProgressBar
    private lateinit var butSignUp: MaterialButton
    private lateinit var butBackgroung: MaterialButton

    private val viewModel by lazy { ViewModelProviders.of(this).get(RegistrationViewModel::class.java) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentRegistrationBinding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_registration, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewmodel = viewModel

        progressBar = binding.progressBar
        butSignUp = binding.butSignUp
        butBackgroung = binding.buttonBack2

        butSignUp.setOnClickListener {
            registr(
                binding.textName.text.toString(),
                viewModel.isPersonChecked.value?.toInt()!!,
                binding.textEmail.text.toString(),
                binding.textPassword.text.toString()
            )
        }


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
                    EditorInfo.IME_ACTION_DONE -> if(viewModel.registrationFormState.value!!.isDataValid) {
                        registr(
                            binding.textName.text.toString(),
                            viewModel.isPersonChecked.value?.toInt()!!,
                            binding.textEmail.text.toString(),
                            binding.textPassword.text.toString()
                        )
                    }
                }
                false
            }

        }

        viewModel.registrationFormState.observe(viewLifecycleOwner, Observer {
            val state = it ?: return@Observer

            binding.inputEmail.error = getString(state.emailError)
            binding.inputName.error = getString(state.nameError)
            binding.inputPassword.error = getString(state.passwordError)
        })

        return binding.root
    }

    private fun getString(id: Int?): String? {
        return if (id == null) null else getString(id)
    }

    private fun registr(name: String, type: Int, email: String, password: String){
        viewModel.createNew(name, type, email, password).observe(viewLifecycleOwner, Observer {
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        onCreateAccountListener?.onCreateNew(it.data!!)
                    }
                    Status.ERROR -> {
                        progressBar.visibility = View.GONE
                        butSignUp.visibility = View.VISIBLE
                        butBackgroung.visibility = View.VISIBLE
                        Toast.makeText(context, it.message, Toast.LENGTH_LONG).show()
                    }
                    Status.LOADING -> {
                        progressBar.visibility = View.VISIBLE
                        butSignUp.visibility = View.INVISIBLE
                        butBackgroung.visibility = View.INVISIBLE
                    }
                }
            }
        })
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