package ru.citadel.rise.login

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.snackbar.Snackbar
import dev.ahmedmourad.bundlizer.Bundlizer
import ru.avangard.rise.R
import ru.citadel.rise.Constants.USER
import ru.citadel.rise.data.model.User
import ru.citadel.rise.login.email.EmailLoginFragment
import ru.citadel.rise.login.email.IOnLogin
import ru.citadel.rise.login.first.IOnNextListener
import ru.citadel.rise.login.first.LoginFirstFragment
import ru.citadel.rise.login.registration.IOnCreateAccountListener
import ru.citadel.rise.login.registration.RegistrationFragment
import ru.citadel.rise.main.MainActivity

class LoginActivity : AppCompatActivity(), IOnNextListener, IOnCreateAccountListener, IOnLogin {

    private var doubleBackToExitPressedOnce = false
    private val mHandler: Handler = Handler()
    private val mRunnable = Runnable { doubleBackToExitPressedOnce = false }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        showFirstFragment()
    }

    override fun onBackPressed() {
        val fragment = supportFragmentManager.findFragmentById(R.id.containerLogin)
        if(fragment is LoginFirstFragment)
            onFirstFragmentBackPressed()
        else {
            showFirstFragment()
        }
    }

    private fun onFirstFragmentBackPressed(){
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed()
            return
        }

        doubleBackToExitPressedOnce = true
        Snackbar
            .make(findViewById(R.id.containerLogin), "Нажмите ещё раз для выхода", Snackbar.LENGTH_SHORT)
            .show()

        mHandler.postDelayed(mRunnable, 2000)
    }

    private fun showFirstFragment(){
        supportFragmentManager.beginTransaction()
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE)
            .replace(R.id.containerLogin, LoginFirstFragment())
            .commitAllowingStateLoss()
    }

//    private fun showLoginEmailFragment(){
//        supportFragmentManager.beginTransaction()
//            .add(
//                R.id.containerLogin,
//                LoginEmailFragment()
//            )
//            .commitNow()
//    }

    private fun showRegistrationFragment(){
        supportFragmentManager.beginTransaction()
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            .replace(R.id.containerLogin, RegistrationFragment())
            .addToBackStack(null)
            .commitAllowingStateLoss()
    }

    private fun showEmailFragment(){
        supportFragmentManager.beginTransaction()
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            .replace(R.id.containerLogin, EmailLoginFragment())
            .commitAllowingStateLoss()
    }

    private fun goOnMain(user: User) {
        val intent = Intent(this, MainActivity::class.java)
        val bundle: Bundle = Bundlizer.bundle(User.serializer(), user)
        intent.putExtra(USER, bundle)
        startActivity(intent)
        finish()
    }

    override fun email() {
        showEmailFragment()
    }

    override fun registration() {
        showRegistrationFragment()
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