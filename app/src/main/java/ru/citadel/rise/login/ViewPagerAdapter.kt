package ru.citadel.rise.login

import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import ru.citadel.rise.login.email.EmailLoginFragment
import ru.citadel.rise.login.registration.RegistrationFragment

class ViewPagerAdapter(activity: AppCompatActivity, private val itemsCount: Int) :
    FragmentStateAdapter(activity) {

    override fun getItemCount(): Int {
        return itemsCount
    }

    override fun createFragment(position: Int) = when(position) {
            0 ->  RegistrationFragment()
            else -> EmailLoginFragment()
        }
}