package ru.citadel.rise.main.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.google.android.material.button.MaterialButton
import ru.avangard.rise.R
import ru.avangard.rise.databinding.FragmentSettingsBinding
import ru.citadel.rise.App
import ru.citadel.rise.IOnBack
import ru.citadel.rise.data.local.PrefsConst
import ru.citadel.rise.data.local.PrefsConst.PREF_LANGUAGE


/**
 * A simple [Fragment] subclass.
 */
class SettingsFragment : Fragment(), IOnBack {

    override fun onBackPressed(): Boolean {
        return true
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentSettingsBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_settings, container, false)

        binding.notificationSwitcher.setChecked(App.prefs.getPref(PrefsConst.PREF_NOTIFICATIONS) as Boolean)
        binding.notificationSwitcher.setOnCheckedChangeListener { checked ->
            App.prefs.setPref(PrefsConst.PREF_NOTIFICATIONS, checked)
        }

        when(App.prefs.getPref(PREF_LANGUAGE) as String) {
            getString(R.string.russian) -> {
                binding.butRussian.strokeWidth = 0
                binding.butRussian.setBackgroundColor(resources.getColor(R.color.accent_red))
                binding.butEnglish.strokeWidth = 4
                binding.butEnglish.setBackgroundColor(resources.getColor(R.color.app_background))
            }
            else -> {
                binding.butEnglish.strokeWidth = 0
                binding.butEnglish.setBackgroundColor(resources.getColor(R.color.accent_red))
                binding.butRussian.strokeWidth = 4
                binding.butRussian.setBackgroundColor(resources.getColor(R.color.app_background))
            }
        }
        binding.butRussian.setOnClickListener {
            (it as MaterialButton).strokeWidth = 0
            it.setBackgroundColor(resources.getColor(R.color.accent_red))
            binding.butEnglish.strokeWidth = 4
            binding.butEnglish.setBackgroundColor(resources.getColor(R.color.app_background))

            App.prefs.setPref(PREF_LANGUAGE, getString(R.string.russian))
        }
        binding.butEnglish.setOnClickListener {
            (it as MaterialButton).strokeWidth = 0
            it.setBackgroundColor(resources.getColor(R.color.accent_red))
            binding.butRussian.strokeWidth = 4
            binding.butRussian.setBackgroundColor(resources.getColor(R.color.app_background))

            App.prefs.setPref(PREF_LANGUAGE, getString(R.string.english))
        }

        return binding.root
    }
}