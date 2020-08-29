package ru.citadel.rise.login

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.citadel.rise.App
import ru.citadel.rise.Constants.USER
import ru.citadel.rise.R
import ru.citadel.rise.data.local.LocalDatabase
import ru.citadel.rise.data.local.LocalRepository
import ru.citadel.rise.data.local.PrefsConst.PREF_KEEP_LOGGIN
import ru.citadel.rise.data.local.PrefsConst.PREF_USER_AUTH
import ru.citadel.rise.data.model.User
import ru.citadel.rise.data.remote.RemoteRepository
import ru.citadel.rise.databinding.ActivityLoginBinding
import ru.citadel.rise.login.email.IOnLogin
import ru.citadel.rise.login.registration.IOnCreateAccountListener
import ru.citadel.rise.main.MainActivity


class LoginActivity : AppCompatActivity(), IOnCreateAccountListener, IOnLogin {

    private lateinit var binding: ActivityLoginBinding

    private var backPressedOnce = false
    private val mHandler: Handler = Handler()
    private val mRunnable = Runnable { backPressedOnce = false }

    private lateinit var localRepository: LocalRepository

    override fun onBackPressed() {
        if (backPressedOnce) {
            finish()
        }

        backPressedOnce = true
        Toast.makeText(
            this@LoginActivity,
            "Нажмите ещё раз для выхода из аккаунта",
            Toast.LENGTH_SHORT
        ).show()
        Handler().postDelayed({ backPressedOnce = false }, 2000)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (intent.getBooleanExtra("LOGOUT", false)) {
            logout()
        }

        binding.apply {
            viewPager.visibility = View.INVISIBLE
            tabLayout.visibility = View.INVISIBLE
            textLoginDescr.visibility = View.VISIBLE
            textConnect.text = resources.getText(R.string.connecting)
            textConnect.visibility = View.VISIBLE
        }

        val dao = LocalDatabase.getDatabase(this)!!.dao()
        localRepository = LocalRepository.getInstance(dao)

        connect()
    }

    private fun logout(){
        lifecycleScope.launch(Dispatchers.IO) {
            try { RemoteRepository().logout() }
            catch (e: Exception) {

            }
        }
    }

    private fun connect(){
        binding.textConnect.text = resources.getText(R.string.connecting)
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                RemoteRepository().getServerStatus().data
            }
            catch (e: Exception) {
                withContext(Dispatchers.Main) { showExp() }
            }
        }
        val keep = App.prefs.getPref(PREF_KEEP_LOGGIN) as Boolean
        if (keep) {
            val auth = (App.prefs.getPref(PREF_USER_AUTH) as String).split('|')
            val id = Integer.parseInt(auth[0])
            lifecycleScope.launch(Dispatchers.IO) {
                val user = localRepository.getUserById(id)
                withContext(Dispatchers.Main) { goOnMain(user) }
            }
        }
        else
            showTabs()
    }

    private fun showExp(){
        binding.textConnect.text = resources.getText(R.string.connection_failed)
        binding.textConnect.setOnClickListener { connect() }
    }

    private fun showTabs(){
//        ObjectAnimator.ofFloat(
//            binding.linearLayout2,
//            "y",
//            64f
//        ).apply {
//            duration = 500
//            start()
//        }
        binding.apply {
            viewPager.visibility = View.VISIBLE
            tabLayout.visibility = View.VISIBLE
            textLoginDescr.visibility = View.INVISIBLE
            textConnect.visibility = View.INVISIBLE
        }

        binding.viewPager.adapter = ViewPagerAdapterLogin(this, 2)

        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            when(position){
                0 -> tab.text = getString(R.string.sign_up)
                else -> tab.text = getString(R.string.sign_in)
            }
        }.attach()

        for (i in 0 until binding.tabLayout.tabCount) {
            val tab = (binding.tabLayout.getChildAt(0) as ViewGroup).getChildAt(i)
            setMarginsInDp(tab, 12, 0, 12, 0)
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

    private fun goOnMain(user: User) {
        val intent = Intent(this, MainActivity::class.java)
        val bundle: Bundle = Bundle().apply { putSerializable(USER, user) }
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