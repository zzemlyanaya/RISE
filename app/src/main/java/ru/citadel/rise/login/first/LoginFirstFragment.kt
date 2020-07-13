package ru.citadel.rise.login.first

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import ru.avangard.rise.R
import ru.avangard.rise.databinding.LoginFirstFragmentBinding

class LoginFirstFragment : Fragment() {

    private var onNextListener: IOnNextListener? = null

    private val viewModel by lazy {
        ViewModelProviders.of(
            this
        ).get(LoginFristViewModel::class.java) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: LoginFirstFragmentBinding = DataBindingUtil.inflate(
            inflater, R.layout.login_first_fragment, container, false)
        binding.lifecycleOwner = viewLifecycleOwner

        binding.butEmail.setOnClickListener { signInWithEmail() }
        binding.butGoogle.setOnClickListener { //signInWithGoogle()
            Toast.makeText(context, "Функция в разработке, но мы стараемся", Toast.LENGTH_SHORT).show()
        }
        binding.butSignUp.setOnClickListener { signUp() }
        return binding.root
    }

    private fun signInWithGoogle(){

    }

    private fun signInWithEmail(){
        onNextListener?.email()
    }

    private fun signUp(){
        onNextListener?.registration()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is IOnNextListener) {
            onNextListener = context
        } else {
            throw RuntimeException("$context must implement IOnNextListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        onNextListener = null
    }
}

interface IOnNextListener {
    //fun onGoogle()
    fun email()
    fun registration()
}