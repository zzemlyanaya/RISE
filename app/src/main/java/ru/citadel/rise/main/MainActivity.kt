package ru.citadel.rise.main

import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentTransaction
import com.fxn.OnBubbleClickListener
import com.google.android.material.snackbar.Snackbar
import dev.ahmedmourad.bundlizer.Bundlizer
import ru.avangard.rise.R
import ru.avangard.rise.databinding.ActivityMainBinding
import ru.citadel.rise.Constants.PROJECTS_ALL
import ru.citadel.rise.Constants.PROJECTS_FAV
import ru.citadel.rise.Constants.PROJECTS_MY
import ru.citadel.rise.Constants.USER
import ru.citadel.rise.data.model.Project
import ru.citadel.rise.data.model.User
import ru.citadel.rise.main.ui.aboutapp.AboutAppFragment
import ru.citadel.rise.main.ui.aboutme.AboutMeFragment
import ru.citadel.rise.main.ui.addeditproject.AddEditProjectFragment
import ru.citadel.rise.main.ui.chat.ChatFragment
import ru.citadel.rise.main.ui.chat.UserShortView
import ru.citadel.rise.main.ui.chats.ChatListFragment
import ru.citadel.rise.main.ui.profile.ProfileFragment
import ru.citadel.rise.main.ui.project.ProjectFragment
import ru.citadel.rise.main.ui.projects.ProjectListFragment
import ru.citadel.rise.main.ui.settings.SettingsFragment


class MainActivity : AppCompatActivity() {

    lateinit var currentUser: User

    private lateinit var binding: ActivityMainBinding

    private var backPressedOnce = false
    private val mHandler: Handler = Handler()
    private val mRunnable = Runnable { backPressedOnce = false }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        currentUser = Bundlizer.unbundle(User.serializer(), intent.extras?.getBundle(USER)!!)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        showProjectsFragment(PROJECTS_ALL)


        binding.navView.addBubbleListener(object : OnBubbleClickListener {
            override fun onBubbleClick(id: Int) {
                when (id) {
                    R.id.navigation_chats -> {
                        if (supportFragmentManager.findFragmentById(R.id.containerMain)  !is ChatListFragment)
                            showChatsFragment()
                    }
                    R.id.navigation_profile -> {
                        if (supportFragmentManager.findFragmentById(R.id.containerMain)  !is ProfileFragment)
                            showProfileFragment()
                    }
                    R.id.navigation_rise -> {
                        if (supportFragmentManager.findFragmentById(R.id.containerMain)  !is ProjectListFragment)
                            showProjectsFragment(PROJECTS_ALL)
                    }
                }
            }
        })


        binding.header.butBack.setOnClickListener { onBackPressed() }

        binding.projectListBar.butAddProject.setOnClickListener { showAddEditProject() }
    }

    override fun onBackPressed() {
        val fragment = supportFragmentManager.findFragmentById(R.id.containerMain)
        when(fragment!!.tag) {
            "chat" -> showChatsFragment()
            "profile", "projects_0", "chats" -> { onBackPressedDouble() }
            "projects_$PROJECTS_FAV", "settings", "about_app", "about_me" -> showProfileFragment()
            "project_$PROJECTS_ALL", "add_edit_project" -> showProjectsFragment(PROJECTS_ALL)
            "project_$PROJECTS_MY" -> showAboutMeFragment()
            "project_$PROJECTS_FAV" -> showProjectsFragment(PROJECTS_FAV)
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
        binding.header.root.visibility = View.VISIBLE
        binding.header.textTitle.visibility = View.VISIBLE
        binding.header.textTitle.text = resources.getText(R.string.dialogs)
        binding.navView.visibility = View.VISIBLE
    }

    private fun hideAllHeaderView(){
        binding.header.textTitle.visibility = View.INVISIBLE
        binding.header.butBack.visibility = View.INVISIBLE
        binding.header.chatHeader.visibility = View.INVISIBLE
        binding.header.butMenuMore.visibility = View.INVISIBLE
        binding.projectListBar.root.visibility = View.GONE
    }

    fun showProjectsFragment(type: Int){
        supportFragmentManager.beginTransaction()
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE)
            .replace(R.id.containerMain, ProjectListFragment.newInstance(type), "projects_$type")
            .commitAllowingStateLoss()
        hideAllHeaderView()
        when(type) {
            PROJECTS_ALL -> {
                binding.header.root.visibility = View.GONE
                binding.navView.visibility = View.VISIBLE
                binding.projectListBar.root.visibility = View.VISIBLE
            }
            PROJECTS_MY -> { }
            PROJECTS_FAV -> {
                binding.header.root.visibility = View.VISIBLE
                binding.navView.visibility = View.GONE
                binding.header.butBack.visibility = View.VISIBLE
                binding.header.textTitle.visibility = View.VISIBLE
                binding.header.textTitle.text = resources.getText(R.string.favourites)
            }
        }
    }

    private fun showProfileFragment(){
        supportFragmentManager.beginTransaction()
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE)
            .replace(R.id.containerMain, ProfileFragment(), "profile")
            .commitAllowingStateLoss()
        hideAllHeaderView()
        binding.header.root.visibility = View.GONE

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
    }

    fun showProjectFragment(project: Project, typeFrom: Int){
        supportFragmentManager.beginTransaction()
            .addToBackStack(null)
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            .replace(R.id.containerMain, ProjectFragment.newInstance(project), "project_$typeFrom")
            .commitAllowingStateLoss()
        binding.header.root.visibility = View.VISIBLE
        binding.projectListBar.root.visibility = View.GONE
        binding.header.butBack.visibility = View.VISIBLE
        binding.navView.visibility = View.INVISIBLE
    }

    fun showSettingsFragment(){
        supportFragmentManager.beginTransaction()
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            .replace(R.id.containerMain, SettingsFragment(), "settings")
            .commitAllowingStateLoss()
        hideAllHeaderView()
        binding.header.root.visibility = View.VISIBLE
        binding.header.textTitle.visibility = View.VISIBLE
        binding.header.textTitle.text = resources.getText(R.string.settings)
        binding.header.butBack.visibility = View.VISIBLE
        binding.navView.visibility = View.INVISIBLE
    }

    fun showAboutAppFragment(){
        supportFragmentManager.beginTransaction()
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE)
            .replace(R.id.containerMain, AboutAppFragment(), "about_app")
            .commitAllowingStateLoss()
        binding.header.root.visibility = View.GONE
        binding.navView.visibility = View.GONE
    }

    fun showAboutMeFragment(){
        supportFragmentManager.beginTransaction()
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE)
            .replace(R.id.containerMain, AboutMeFragment(), "about_me")
            .commitAllowingStateLoss()
        binding.header.root.visibility = View.GONE
        binding.navView.visibility = View.GONE
    }

    fun showAddEditProject(){
        supportFragmentManager.beginTransaction()
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE)
            .replace(R.id.containerMain,
                AddEditProjectFragment(), "add_edit_project")
            .commitAllowingStateLoss()
        binding.header.root.visibility = View.GONE
        binding.projectListBar.root.visibility = View.GONE
        binding.navView.visibility = View.GONE
    }

}