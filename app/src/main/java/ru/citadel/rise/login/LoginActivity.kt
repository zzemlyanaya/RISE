package ru.citadel.rise.login

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.DisplayMetrics
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.android.material.tabs.TabLayoutMediator
import dev.ahmedmourad.bundlizer.Bundlizer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.avangard.rise.R
import ru.avangard.rise.databinding.ActivityLoginBinding
import ru.citadel.rise.Constants.USER
import ru.citadel.rise.data.RemoteRepository
import ru.citadel.rise.data.model.User
import ru.citadel.rise.login.email.IOnLogin
import ru.citadel.rise.login.registration.IOnCreateAccountListener
import ru.citadel.rise.main.MainActivity


class LoginActivity : AppCompatActivity(), IOnCreateAccountListener, IOnLogin {

    private lateinit var binding: ActivityLoginBinding

    private var doubleBackToExitPressedOnce = false
    private val mHandler: Handler = Handler()
    private val mRunnable = Runnable { doubleBackToExitPressedOnce = false }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {
            viewPager.visibility = View.INVISIBLE
            tabLayout.visibility = View.INVISIBLE
            textLoginDescr.visibility = View.VISIBLE
            textConnect.text = "Соединяем с сервером..."
            textConnect.visibility = View.VISIBLE
        }

        connect()
    }

    private fun connect(){
        binding.textConnect.text = "Соединяем с сервером..."
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val status = RemoteRepository().getServerStatus().data
                withContext(Dispatchers.Main) { showTabs() }
            }
            catch (e: Exception) {
                withContext(Dispatchers.Main) { showExp() }
            }

        }
    }

    private fun showExp(){
        binding.textConnect.text = "Сервер не доступен. Нажмите для повторного соединения."
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

        binding.viewPager.adapter = ViewPagerAdapter(this, 2)

        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            when(position){
                0 -> tab.text = getString(R.string.sign_up_short)
                else -> tab.text = getString(R.string.sign_in)
            }
        }.attach()

        for (i in 0 until binding.tabLayout.tabCount) {
            val tab = (binding.tabLayout.getChildAt(0) as ViewGroup).getChildAt(i)
            setMarginsInDp(tab, 12, 0, 12, 0)
        }
    }

    private fun dpToPx(dp: Int): Float {
        val displayMetrics: DisplayMetrics = resources.displayMetrics
        return (dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT))
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