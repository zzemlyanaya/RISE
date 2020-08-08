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
import ru.avangard.rise.R
import ru.avangard.rise.databinding.FragmentAboutMeBinding
import ru.citadel.rise.main.MainActivity

class AboutMeFragment : Fragment() {

    private lateinit var viewModel: AboutMeViewModel
    private val user by lazy { (activity as MainActivity).currentUser }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentAboutMeBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_about_me, container, false)

        binding.viewPager.adapter = ViewPagerAdapterAboutMe(requireActivity() as AppCompatActivity, 2)

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

        return binding.root
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
        // TODO: Use the ViewModel
    }

}