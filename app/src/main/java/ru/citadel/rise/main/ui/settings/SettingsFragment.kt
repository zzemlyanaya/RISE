package ru.citadel.rise.main.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import ru.avangard.rise.R
import ru.avangard.rise.databinding.FragmentSettingsBinding
import ru.citadel.rise.App
import ru.citadel.rise.IOnBack
import ru.citadel.rise.data.local.PrefsConst


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

        val lang = App.prefs.getPref(PrefsConst.PREF_LANGUAGE) as String
        val index = resources.getStringArray(R.array.languages).indexOf(lang)

        binding.languageSpinner.adapter =
            ArrayAdapter<CharSequence>(
                requireContext(),
                R.layout.item_spinner_text,
                resources.getStringArray(R.array.languages)
            )
        binding.languageSpinner.setSelection(index, true)

        binding.languageSpinner.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>, view: View, pos: Int,
                id: Long
            ) {
                App.prefs.setPref(PrefsConst.PREF_LANGUAGE, parent.getItemAtPosition(pos))
            }

            override fun onNothingSelected(arg0: AdapterView<*>?) {}
        }

        return binding.root
    }
}