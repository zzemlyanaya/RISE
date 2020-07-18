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
import ru.avangard.rise.R
import ru.avangard.rise.databinding.RegistrationFragmentBinding
import ru.citadel.rise.Status
import ru.citadel.rise.afterTextChanged
import ru.citadel.rise.data.model.User
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
        val binding: RegistrationFragmentBinding = DataBindingUtil.inflate(
                inflater, R.layout.registration_fragment, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewmodel = viewModel

        progressBar = binding.progressBar
        butSignUp = binding.butSignUp
        butBackgroung = binding.buttonBack2

        butSignUp.setOnClickListener {
            val user = User(
                binding.textEmail.text.hashCode(),
                binding.textEmail.text.toString(),
                binding.textName.text.toString(),
                viewModel.isPersonChecked.value?.toInt()!!,
                null, null, null, null
            )
            registr(user)
        }
        binding.butBackToFirst.setOnClickListener { activity?.onBackPressed() }

        val widthDp = (resources.displayMetrics.run { widthPixels / density }).toInt()
        val cardSize = (widthDp*1.1).toInt()

        binding.cardCompany.layoutParams.width = cardSize
        binding.cardPerson.layoutParams.width = cardSize

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
                        val user = User(
                            binding.textEmail.text.hashCode(),
                            binding.textEmail.text.toString(),
                            binding.textName.text.toString(),
                            viewModel.isPersonChecked.value?.toInt()!!,
                            null, null, null, null
                        )
                        registr(user)
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

    private fun registr(user: User){
        viewModel.createNew(user).observe(viewLifecycleOwner, Observer {
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        onCreateAccountListener?.onCreateNew(user)
                    }
                    Status.ERROR -> {
                        progressBar.visibility = View.GONE
                        butSignUp.visibility = View.VISIBLE
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