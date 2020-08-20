package ru.citadel.rise.main.ui.aboutme

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import ru.citadel.rise.Constants.USER
import ru.citadel.rise.R
import ru.citadel.rise.Status
import ru.citadel.rise.data.local.LocalDatabase
import ru.citadel.rise.data.local.LocalRepository
import ru.citadel.rise.data.model.Resource
import ru.citadel.rise.data.model.User
import ru.citadel.rise.databinding.FragmentEditUserBinding
import ru.citadel.rise.main.EditUserViewModelFactory
import ru.citadel.rise.main.MainActivity

class EditUserFragment : Fragment() {

    companion object {
        @JvmStatic
        fun newInstance(user: User) =
            EditUserFragment().apply {
                arguments = Bundle().apply {
                    val bundle = Bundle().apply { putSerializable(USER, user) }
                    putBundle(USER, bundle)
                }
            }
    }

    private lateinit var user: User
    private lateinit var viewModel: EditUserViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        user = requireArguments().getBundle(USER)!!.getSerializable(USER) as User
        val binding: FragmentEditUserBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_edit_user, container, false)

        binding.textUserName.setText(user.name)
        binding.textUserCity.setText(user.city ?: getString(R.string.city))
        binding.textUserCountry.setText(user.country ?: getString(R.string.country))
        binding.textUserAbout.setText(user.about ?: getString(R.string.about_me))

        binding.butCancel.setOnClickListener {
            (requireActivity() as MainActivity).onBackPressed()
        }
        binding.butSaveUser.setOnClickListener {
            if (binding.textUserName.text.isNullOrBlank())
                return@setOnClickListener
            user.name = binding.textUserName.text.toString()
            user.city = binding.textUserCity.text.toString()
            user.country = binding.textUserCountry.text.toString()
            user.about = binding.textUserAbout.text.toString()

            viewModel.updateUser(user).observe(viewLifecycleOwner, Observer { showStatus(it) })
        }

        return binding.root
    }

    private fun showStatus(it: Resource<String?>) {
        when(it.status){
            Status.LOADING -> {
                Toast.makeText(context, getString(R.string.executing), Toast.LENGTH_SHORT).show()
            }
            Status.ERROR -> {
                Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
                Log.d("SERVER", it.message.toString())
            }
            Status.SUCCESS -> {
                Toast.makeText(context, getString(R.string.successful), Toast.LENGTH_SHORT).show()
                (requireActivity() as MainActivity).currentUser = user
                (requireActivity() as MainActivity).onBackPressed()
            }
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val dao = LocalDatabase.getDatabase(requireContext())!!.dao()
        viewModel =
            ViewModelProviders
                .of(this, EditUserViewModelFactory(LocalRepository.getInstance(dao)))
                .get(EditUserViewModel::class.java)

    }

}