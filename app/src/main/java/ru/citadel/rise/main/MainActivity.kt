package ru.citadel.rise.main

import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import dev.ahmedmourad.bundlizer.Bundlizer
import ru.avangard.rise.R
import ru.avangard.rise.databinding.ActivityMainBinding
import ru.citadel.rise.Constants.USER
import ru.citadel.rise.data.model.Project
import ru.citadel.rise.data.model.User
import ru.citadel.rise.main.ui.chat.ChatFragment
import ru.citadel.rise.main.ui.chat.UserShortView
import ru.citadel.rise.main.ui.chats.ChatListFragment
import ru.citadel.rise.main.ui.profile.ProfileFragment
import ru.citadel.rise.main.ui.project.ProjectFragment
import ru.citadel.rise.main.ui.projects.ProjectListFragment
import ru.citadel.rise.main.ui.settings.SettingsFragment


class MainActivity : FragmentActivity() {

    lateinit var currentUser: User

    private lateinit var binding: ActivityMainBinding

    private var backPressedOnce = false
    private val mHandler: Handler = Handler()
    private val mRunnable = Runnable { backPressedOnce = false }

    private val mOnNavigationItemSelectedListener
            = BottomNavigationView.OnNavigationItemSelectedListener {
            menuItem -> when (menuItem.itemId) {
        R.id.navigation_chats -> {
            if (supportFragmentManager.findFragmentById(R.id.containerMain)  !is ChatListFragment)
                showChatsFragment()
            return@OnNavigationItemSelectedListener true
        }
        R.id.navigation_profile -> {
            if (supportFragmentManager.findFragmentById(R.id.containerMain)  !is ProfileFragment)
                showProfileFragment()
            return@OnNavigationItemSelectedListener true
        }
        R.id.navigation_rise -> {
            if (supportFragmentManager.findFragmentById(R.id.containerMain)  !is ProjectListFragment)
                showProjectsFragment(0)
            return@OnNavigationItemSelectedListener true
        }
    }
        false
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        currentUser = Bundlizer.unbundle(User.serializer(), intent.extras?.getBundle(USER)!!)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportFragmentManager.beginTransaction()
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            .replace(R.id.containerMain, ProjectListFragment.newInstance(0), "projects_0")
            .commitAllowingStateLoss()

        binding.navView.apply {
            setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
            selectedItemId = R.id.navigation_rise
        }

        binding.header.butBack.setOnClickListener { onBackPressed() }
    }

    override fun onBackPressed() {
        val fragment = supportFragmentManager.findFragmentById(R.id.containerMain)
        when(fragment!!.tag) {
            "chat" -> showChatsFragment()
            "profile", "projects_0", "chats" -> { onBackPressedDouble() }
            "projects_1", "projects_2", "settings" -> showProfileFragment()
            "project_0" -> showProjectsFragment(0)
            "project_1" -> showProjectsFragment(1)
            "project_2" -> showProjectsFragment(2)
            else -> {}
        }
    }

    private fun onBackPressedDouble(){
        if (backPressedOnce) {
            finish()
        }

        backPressedOnce = true
        Snackbar.make(binding.root, "Нажмите ещё раз для выхода", Snackbar.LENGTH_SHORT).show()
        mHandler.postDelayed(mRunnable, 2000)
    }

    private fun showChatsFragment(){
        supportFragmentManager.beginTransaction()
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE)
            .replace(R.id.containerMain, ChatListFragment(), "chats")
            .commitAllowingStateLoss()
        hideAllHeaderView()
        binding.header.textTitle.visibility = View.VISIBLE
        binding.header.textTitle.text = "Диалоги"
        binding.navView.visibility = View.VISIBLE
    }

    private fun hideAllHeaderView(){
        binding.header.labelRise.visibility = View.INVISIBLE
        binding.header.textTitle.visibility = View.INVISIBLE
        binding.header.butBack.visibility = View.INVISIBLE
        binding.header.chatHeader.visibility = View.INVISIBLE
        binding.header.butMenuMore.visibility = View.INVISIBLE
    }

    fun showProjectsFragment(type: Int){
        supportFragmentManager.beginTransaction()
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE)
            .replace(R.id.containerMain, ProjectListFragment.newInstance(type), "projects_$type")
            .commitAllowingStateLoss()
        hideAllHeaderView()
        when(type) {
            0 -> {
                binding.header.labelRise.visibility = View.VISIBLE
                binding.navView.visibility = View.VISIBLE
            }
            1 -> {
                binding.navView.visibility = View.INVISIBLE
                binding.header.butBack.visibility = View.VISIBLE
                binding.header.textTitle.visibility = View.VISIBLE
                binding.header.textTitle.text = "Мои проекты"
            }
            else -> {
                binding.navView.visibility = View.INVISIBLE
                binding.header.butBack.visibility = View.VISIBLE
                binding.header.textTitle.visibility = View.VISIBLE
                binding.header.textTitle.text = "Избранное"
            }
        }
    }

    private fun showProfileFragment(){
        supportFragmentManager.beginTransaction()
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE)
            .replace(R.id.containerMain, ProfileFragment(), "profile")
            .commitAllowingStateLoss()
        hideAllHeaderView()
        binding.header.textTitle.visibility = View.VISIBLE
        binding.header.textTitle.text = "Профиль"

        binding.navView.visibility = View.VISIBLE
    }

    fun showChatFragment(user: UserShortView){
        binding.navView.visibility = View.GONE
        supportFragmentManager.beginTransaction()
            .addToBackStack(null)
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            .replace(R.id.containerMain, ChatFragment(), "chat")
            .commitAllowingStateLoss()
        hideAllHeaderView()
        binding.header.butBack.visibility = View.VISIBLE
        binding.header.chatHeader.visibility = View.VISIBLE
        binding.header.butMenuMore.visibility = View.VISIBLE
        binding.header.chatName.text = user.name
        when(user.status){
            1 -> {
                binding.header.chatStatus.text = "online"
                binding.header.chatStatusIcon.visibility = View.VISIBLE
            }
            else -> {
                binding.header.chatStatus.text = "gone"
                binding.header.chatStatusIcon.visibility = View.INVISIBLE
            }

        }
    }

    fun showProjectFragment(project: Project, typeFrom: Int){
        supportFragmentManager.beginTransaction()
            .addToBackStack(null)
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            .replace(R.id.containerMain, ProjectFragment.newInstance(project), "project_$typeFrom")
            .commitAllowingStateLoss()
        binding.header.butBack.visibility = View.VISIBLE
        binding.navView.visibility = View.INVISIBLE
    }

    fun showSettingsFragment(){
        supportFragmentManager.beginTransaction()
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            .replace(R.id.containerMain, SettingsFragment(), "settings")
            .commitAllowingStateLoss()
        hideAllHeaderView()
        binding.header.textTitle.visibility = View.VISIBLE
        binding.header.textTitle.text = "Настройки"
        binding.header.butBack.visibility = View.VISIBLE
        binding.navView.visibility = View.INVISIBLE
    }

}