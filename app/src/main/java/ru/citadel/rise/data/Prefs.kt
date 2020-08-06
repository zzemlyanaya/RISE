package ru.citadel.rise.data

import com.kryptoprefs.context.KryptoContext
import com.kryptoprefs.preferences.KryptoPrefs

object PrefsConst {
    const val PREFS_NAME = "RISEPrefs"
    const val PREF_KEEP_LOGGIN = "keep loggin"
    const val PREF_LANGUAGE = "language"
    const val PREF_NOTIFICATIONS = "notofications"
}

class Prefs(prefs: KryptoPrefs): KryptoContext(prefs) {
    private val isKeepLoggin = boolean(PrefsConst.PREF_KEEP_LOGGIN, false)
    private val language = string(PrefsConst.PREF_LANGUAGE, "Русский", true)
    private val notifications = boolean(PrefsConst.PREF_NOTIFICATIONS, true)

    fun setPref(key: String, value: Any){
        when(key){
            PrefsConst.PREF_KEEP_LOGGIN -> isKeepLoggin.put(value as Boolean)
            PrefsConst.PREF_LANGUAGE -> language.put(value as String)
            PrefsConst.PREF_NOTIFICATIONS -> notifications.put(value as Boolean)
            else -> throw Exception("Unknown key!")
        }
    }

    fun getPref(key: String): Any = when(key) {
        PrefsConst.PREF_KEEP_LOGGIN -> isKeepLoggin.get()
        PrefsConst.PREF_LANGUAGE -> language.get()
        PrefsConst.PREF_NOTIFICATIONS -> notifications.get()
        else -> throw Exception("Unknown key!")
    }
}