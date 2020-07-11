package ru.citadel.rise.main

import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.bottomnavigation.BottomNavigationView
import ru.avangard.rise.R
import ru.avangard.rise.databinding.ActivityMainBinding
import ru.citadel.rise.main.ui.chats.ChatsFragment
import ru.citadel.rise.main.ui.profile.ProfileFragment
import ru.citadel.rise.main.ui.projects.ProjectFragment


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private var backPressedOnce = false
    private val mHandler: Handler = Handler()
    private val mRunnable = Runnable { backPressedOnce = false }

    private val mOnNavigationItemSelectedListener
            = BottomNavigationView.OnNavigationItemSelectedListener {
            menuItem -> when (menuItem.itemId) {
        R.id.navigation_chats -> {
            if (supportFragmentManager.findFragmentById(R.id.containerMain)  !is ChatsFragment)
                showChatFragment()
            return@OnNavigationItemSelectedListener true
        }
        R.id.navigation_profile -> {
            if (supportFragmentManager.findFragmentById(R.id.containerMain)  !is ProfileFragment)
                showProfileFragment()
            return@OnNavigationItemSelectedListener true
        }
        R.id.navigation_rise -> {
            if (supportFragmentManager.findFragmentById(R.id.containerMain)  !is ProjectFragment)
                showProjectFragment()
            return@OnNavigationItemSelectedListener true
        }
    }
        false
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportFragmentManager.beginTransaction()
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            .replace(R.id.containerMain, ProjectFragment())
            .commitAllowingStateLoss()

        binding.navView.apply {
            setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
            selectedItemId = R.id.navigation_rise
        }
    }

    private fun showChatFragment(){
        supportFragmentManager.beginTransaction()
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            .replace(R.id.containerMain, ChatsFragment())
            .commitAllowingStateLoss()
        hideAllHeaderView()
        binding.header.textTitle.visibility = View.VISIBLE
        binding.header.textTitle.text = "Диалоги"
    }

    private fun hideAllHeaderView(){
        binding.header.labelRise.visibility = View.INVISIBLE
        binding.header.textTitle.visibility = View.INVISIBLE
        binding.header.butBack.visibility = View.INVISIBLE
    }

    private fun showProjectFragment(){
        supportFragmentManager.beginTransaction()
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            .replace(R.id.containerMain, ProjectFragment())
            .commitAllowingStateLoss()
        hideAllHeaderView()
        binding.header.labelRise.visibility = View.VISIBLE
    }

    private fun showProfileFragment(){
        supportFragmentManager.beginTransaction()
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            .replace(R.id.containerMain, ProfileFragment())
            .commitAllowingStateLoss()
        hideAllHeaderView()
        binding.header.textTitle.visibility = View.VISIBLE
        binding.header.textTitle.text = "Профиль"
    }

}