package ru.citadel.rise.login

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import dev.ahmedmourad.bundlizer.Bundlizer
import ru.avangard.rise.R
import ru.citadel.rise.Constants.USER
import ru.citadel.rise.data.model.User
import ru.citadel.rise.login.email.IOnLogin
import ru.citadel.rise.login.registration.IOnCreateAccountListener
import ru.citadel.rise.main.MainActivity

class LoginActivity : AppCompatActivity(), IOnCreateAccountListener, IOnLogin {

    private lateinit var viewPager2: ViewPager2

    private var doubleBackToExitPressedOnce = false
    private val mHandler: Handler = Handler()
    private val mRunnable = Runnable { doubleBackToExitPressedOnce = false }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        viewPager2 = findViewById(R.id.viewPager)
        val tabs = findViewById<TabLayout>(R.id.tabLayout)

        viewPager2.adapter = ViewPagerAdapter(this, 2)

        TabLayoutMediator(tabs, viewPager2) { tab, position ->
            when(position){
                0 -> tab.text = getString(R.string.sign_up_short)
                else -> tab.text = getString(R.string.sign_in)
            }
        }.attach()

    }

    private fun goOnMain(user: User) {
        val intent = Intent(this, MainActivity::class.java)
        val bundle: Bundle = Bundlizer.bundle(User.serializer(), user)
        intent.putExtra(USER, bundle)
        startActivity(intent)
        finish()
    }

    override fun onCreateNew(user: User) {
        goOnMain(user)
    }

    override fun onLogin(user: User) {
        goOnMain(user)
    }

    override fun onDestroy() {
        super.onDestroy()
        mHandler.removeCallbacks(mRunnable)
    }
}