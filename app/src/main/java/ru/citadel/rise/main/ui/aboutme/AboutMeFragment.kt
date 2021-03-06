package ru.citadel.rise.main.ui.aboutme

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.citadel.rise.Constants.LIST_TYPE
import ru.citadel.rise.Constants.PROJECTS_MY
import ru.citadel.rise.Constants.USER
import ru.citadel.rise.R
import ru.citadel.rise.data.local.LocalDatabase
import ru.citadel.rise.data.local.LocalRepository
import ru.citadel.rise.data.model.User
import ru.citadel.rise.databinding.FragmentAboutMeBinding
import ru.citadel.rise.main.MainActivity
import ru.citadel.rise.main.ui.chat.UserShortView

class AboutMeFragment : Fragment() {

    private lateinit var viewModel: AboutMeViewModel
    private lateinit var user: User
    private var curUserId = 0
    private var listType = 0

    private lateinit var localRepository: LocalRepository

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        user = requireArguments().getBundle(USER)!!.getSerializable(USER) as User
        listType = arguments?.getInt(LIST_TYPE) ?: PROJECTS_MY
        curUserId = (requireActivity() as MainActivity).currentUser.userId

        val dao = LocalDatabase.getDatabase(requireContext())!!.dao()
        localRepository = LocalRepository.getInstance(dao)

        val binding: FragmentAboutMeBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_about_me, container, false)

        binding.viewPager.adapter = ViewPagerAdapterAboutMe(
            requireActivity() as AppCompatActivity, 2,
            user, listType
        )

        TabLayoutMediator(binding.aboutMeTabBar, binding.viewPager) { tab, position ->
            when(position){
                0 -> tab.text = getString(R.string.projects)
                else -> tab.text = getString(R.string.about_me_about)
            }
        }.attach()

        for (i in 0 until binding.aboutMeTabBar.tabCount) {
            val tab = (binding.aboutMeTabBar.getChildAt(0) as ViewGroup).getChildAt(i)
            setMarginsInDp(tab, 12, 0, 12, 0)
        }

        binding.aboutMeName.text = user.name

        if (curUserId == user.userId)
            binding.aboutMeButContact.apply {
                text = getString(R.string.edit)
                setOnClickListener {
                    (requireActivity() as MainActivity).showEditUserFragment()
                }
            }
        else
            binding.aboutMeButContact.apply {
                text = getString(R.string.but_contact)
                setOnClickListener { contact() }
            }

        return binding.root
    }

    private fun contact() = run {
        CoroutineScope(Dispatchers.IO).launch {
            val id = localRepository.getChatIdByUsers(curUserId, user.userId)
            val shortView = UserShortView(user.userId, user.name)
            withContext(Dispatchers.Main) {
                (requireActivity() as MainActivity).showChatFragment(shortView, id ?: 0)
            }
        }

    }


    private fun setMarginsInDp(view: View, left: Int, top: Int, right: Int, bottom: Int){
        if(view.layoutParams is ViewGroup.MarginLayoutParams){
            val screenDensity: Float = view.context.resources.displayMetrics.density
            val params: ViewGroup.MarginLayoutParams = view.layoutParams as ViewGroup.MarginLayoutParams
            params.setMargins(left*screenDensity.toInt(), top*screenDensity.toInt(), right*screenDensity.toInt(), bottom*screenDensity.toInt())
            view.requestLayout()
        }
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(AboutMeViewModel::class.java)
    }

    companion object {
        @JvmStatic
        fun newInstance(user: User, listType: Int): AboutMeFragment {
            val args = Bundle().apply {
                val bundle = Bundle().apply { putSerializable(USER, user) }
                putBundle(USER, bundle)
                putInt(LIST_TYPE, listType)
            }
            val fragment = AboutMeFragment()
            fragment.arguments = args
            return fragment
        }
    }

}