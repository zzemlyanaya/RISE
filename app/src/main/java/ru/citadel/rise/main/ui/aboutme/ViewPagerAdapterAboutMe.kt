package ru.citadel.rise.main.ui.aboutme

import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import ru.citadel.rise.data.model.User
import ru.citadel.rise.main.ui.projects.ProjectListFragment

class ViewPagerAdapterAboutMe(activity: AppCompatActivity, private val itemsCount: Int,
private val user: User, private val list_type: Int) :
    FragmentStateAdapter(activity) {

    override fun getItemCount(): Int {
        return itemsCount
    }

    override fun createFragment(position: Int) = when(position) {
            0 ->  ProjectListFragment.newInstance(list_type, user.userId)
            else -> AboutMeAboutFragment.newInstance(user)
        }
}