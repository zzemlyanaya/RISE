package ru.citadel.rise.login.email

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
import ru.citadel.rise.IOnBack
import ru.citadel.rise.R
import ru.citadel.rise.Status
import ru.citadel.rise.afterTextChanged
import ru.citadel.rise.data.model.User
import ru.citadel.rise.databinding.FragmentLoginBinding

class EmailLoginFragment : Fragment(), IOnBack {

    override fun onBackPressed(): Boolean {
        return true
    }

    private lateinit var progressBar: ProgressBar
    private lateinit var butSignIn: MaterialButton
    private lateinit var butBackground: MaterialButton

    private var onLogin: IOnLogin? = null
    private val viewModel by lazy {ViewModelProviders.of(this).get(EmailLoginViewModel::class.java)}

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentLoginBinding
                = DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewmodel = viewModel

        progressBar = binding.loginProgress
        butSignIn = binding.butSignIn
        butBackground = binding.buttonBack1

        butSignIn.setOnClickListener {
            authorize(
                binding.textLogin.text.toString().hashCode(),
                binding.textPasswordLogin.text.toString(),
                binding.checkKeepLogin.isChecked
            )
        }

        viewModel.loginFormState.observe(viewLifecycleOwner, Observer {
            val loginState = it ?: return@Observer

            binding.inputLogin.error = getString(loginState.usernameError)
            binding.inputPasswordLogin.error = getString(loginState.passwordError)
        })

        binding.textLogin.afterTextChanged {
            viewModel.loginDataChanged(
                binding.textLogin.text.toString(),
                binding.textPasswordLogin.text.toString()
            )
        }

        binding.textPasswordLogin.apply {
            afterTextChanged {
                viewModel.loginDataChanged(
                    binding.textLogin.text.toString(),
                    binding.textPasswordLogin.text.toString()
                )
            }

            setOnEditorActionListener { _, actionId, _ ->
                when (actionId) {
                    EditorInfo.IME_ACTION_DONE ->
                        if(viewModel.loginFormState.value!!.isDataValid)
                        authorize(
                            binding.textLogin.text.toString().hashCode(),
                            binding.textPasswordLogin.text.toString(),
                            binding.checkKeepLogin.isChecked
                        )
                }
                false
            }
        }


        return binding.root
    }

    private fun getString(id: Int?): String? {
        return if (id == null) null else getString(id)
    }

    private fun authorize(id: Int, password: String, isKeepLogin: Boolean){
        viewModel.authorize(id, password, isKeepLogin).observe(viewLifecycleOwner, Observer {
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        resource.data?.let { user -> onLogin?.onLogin(user) }
                    }
                    Status.ERROR -> {
                        progressBar.visibility = View.GONE
                        butSignIn.visibility = View.VISIBLE
                        butBackground.visibility = View.VISIBLE
                        Toast.makeText(context, it.message, Toast.LENGTH_LONG).show()
                    }
                    Status.LOADING -> {
                        progressBar.visibility = View.VISIBLE
                        butSignIn.visibility = View.INVISIBLE
                        butBackground.visibility = View.INVISIBLE
                    }
                }
            }
        })
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is IOnLogin)
            onLogin = context
        else
            throw Exception("Must implement IOnBack!")
    }

    override fun onDetach() {
        super.onDetach()
        onLogin = null
    }
}

interface IOnLogin {
    fun onLogin(user: User)
}

