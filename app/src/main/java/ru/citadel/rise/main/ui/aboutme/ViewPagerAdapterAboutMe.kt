package ru.citadel.rise.main.ui.aboutme

import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import ru.citadel.rise.Constants.PROJECTS_MY
import ru.citadel.rise.main.ui.projects.ProjectListFragment

class ViewPagerAdapterAboutMe(activity: AppCompatActivity, private val itemsCount: Int) :
    FragmentStateAdapter(activity) {

    override fun getItemCount(): Int {
        return itemsCount
    }

    override fun createFragment(position: Int) = when(position) {
            0 ->  ProjectListFragment.newInstance(PROJECTS_MY)
            else -> AboutMeAboutFragment()
        }
}