package ru.citadel.rise.main

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.fxn.OnBubbleClickListener
import ru.citadel.rise.App
import ru.citadel.rise.Constants.PROJECTS_ALL
import ru.citadel.rise.Constants.PROJECTS_BY_USER
import ru.citadel.rise.Constants.PROJECTS_FAV
import ru.citadel.rise.Constants.PROJECTS_MY
import ru.citadel.rise.Constants.TYPE_AUTHOR
import ru.citadel.rise.Constants.USER
import ru.citadel.rise.R
import ru.citadel.rise.data.local.LocalDatabase
import ru.citadel.rise.data.local.LocalRepository
import ru.citadel.rise.data.local.PrefsConst.PREF_KEEP_LOGGIN
import ru.citadel.rise.data.model.Project
import ru.citadel.rise.data.model.User
import ru.citadel.rise.databinding.ActivityMainBinding
import ru.citadel.rise.login.LoginActivity
import ru.citadel.rise.main.ui.aboutapp.AboutAppFragment
import ru.citadel.rise.main.ui.aboutme.AboutMeFragment
import ru.citadel.rise.main.ui.aboutme.EditUserFragment
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
    private lateinit var viewModel: MainViewModel

    private lateinit var binding: ActivityMainBinding

    private var backPressedOnce = false
    private val mHandler: Handler = Handler()
    private val mRunnable = Runnable { backPressedOnce = false }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        currentUser = intent.extras!!.getBundle(USER)!!.getSerializable(USER) as User

        val dao = LocalDatabase.getDatabase(this)!!.dao()
        viewModel = ViewModelProviders.of(this, MainViewModelFactory(LocalRepository.getInstance(dao), currentUser)).get(MainViewModel::class.java)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        showProjectsFragment(PROJECTS_ALL, currentUser.userId)

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
                            showProjectsFragment(PROJECTS_ALL, currentUser.userId)
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
            "projects_$PROJECTS_FAV", "settings", "about_app", "about_me_$PROJECTS_MY" -> showProfileFragment()
            "project_$PROJECTS_ALL" -> showProjectsFragment(PROJECTS_ALL, currentUser.userId)
            "project_$PROJECTS_MY", "edit_user" -> showAboutMeFragment(currentUser)
            "project_$PROJECTS_FAV" -> showProjectsFragment(PROJECTS_FAV, currentUser.userId)
            "add_edit_project" -> (fragment as AddEditProjectFragment).onBack()
            else -> {}
        }
    }

    private fun onBackPressedDouble(){
        if (backPressedOnce) {
            App.prefs.setPref(PREF_KEEP_LOGGIN, false)
            val intent = Intent(this, LoginActivity::class.java)
            viewModel.logout().observe(this, Observer {
                if (it == "OK"){
                    startActivity(intent)
                    finish()
                } else
                    Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
            })
        }

        backPressedOnce = true
        Toast.makeText(this@MainActivity, "Нажмите ещё раз для выхода из аккаунта", Toast.LENGTH_SHORT).show()
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

    fun showProjectsFragment(type: Int, userId: Int){
        supportFragmentManager.beginTransaction()
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE)
            .replace(R.id.containerMain, ProjectListFragment.newInstance(type, userId), "projects_$type")
            .commitAllowingStateLoss()
        hideAllHeaderView()
        if (currentUser.type == TYPE_AUTHOR)
            binding.projectListBar.cardAddProject.visibility = View.VISIBLE
        else
            binding.projectListBar.cardAddProject.visibility = View.GONE
        when(type) {
            PROJECTS_ALL -> {
                binding.projectListBar.root.visibility = View.VISIBLE
                binding.header.root.visibility = View.GONE
                binding.navView.visibility = View.VISIBLE
            }
            PROJECTS_MY, PROJECTS_BY_USER -> { }
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

    fun showChatFragment(user: UserShortView, chatId: Int){
        binding.navView.visibility = View.GONE
        supportFragmentManager.beginTransaction()
            .addToBackStack(null)
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            .replace(R.id.containerMain, ChatFragment.newInstance(user, chatId), "chat")
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
            .replace(R.id.containerMain, ProjectFragment.newInstance(project, currentUser.userId), "project_$typeFrom")
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

    fun showAboutMeFragment(user: User){
        val listType = if (user == currentUser) PROJECTS_MY else PROJECTS_BY_USER
        supportFragmentManager.beginTransaction()
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE)
            .replace(R.id.containerMain, AboutMeFragment.newInstance(user, listType), "about_me_$listType")
            .commitAllowingStateLoss()
        binding.header.root.visibility = View.GONE
        binding.navView.visibility = View.GONE
    }

    private fun showAddEditProject(){
        supportFragmentManager.beginTransaction()
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE)
            .replace(R.id.containerMain,
                AddEditProjectFragment.newInstance(null), "add_edit_project")
            .commitAllowingStateLoss()
        binding.header.root.visibility = View.GONE
        binding.projectListBar.root.visibility = View.GONE
        binding.navView.visibility = View.GONE
    }

    fun showAddEditProject(project: Project){
        supportFragmentManager.beginTransaction()
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            .replace(R.id.containerMain,
                AddEditProjectFragment.newInstance(project), "add_edit_project")
            .commitAllowingStateLoss()
        binding.header.root.visibility = View.GONE
        binding.projectListBar.root.visibility = View.GONE
        binding.navView.visibility = View.GONE
    }

    fun showEditUserFragment(){
        supportFragmentManager.beginTransaction()
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            .replace(R.id.containerMain, EditUserFragment.newInstance(currentUser), "edit_user")
            .commitAllowingStateLoss()
        binding.header.root.visibility = View.GONE
        binding.projectListBar.root.visibility = View.GONE
        binding.navView.visibility = View.GONE
    }


}