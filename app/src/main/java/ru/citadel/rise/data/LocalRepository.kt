package ru.citadel.rise.data

import ru.citadel.rise.App

class LocalRepository {
    private val prefs = App.prefs

    fun setPref(key: String, value: Any) = prefs.setPref(key, value)

    fun getPref(key:String): Any = prefs.getPref(key)
}