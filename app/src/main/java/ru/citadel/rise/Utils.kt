package ru.citadel.rise

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText

interface IOnBack{
    fun onBackPressed(): Boolean
}

object Constants {
    const val USER = "user"
    const val PROJECT = "project"
    const val LIST_TYPE = "type"
    const val CHAT_ID = "chatId"

    const val PROJECTS_ALL = 0
    const val PROJECTS_MY = 1
    const val PROJECTS_FAV = 2
    const val PROJECTS_BY_USER = 3

    const val TYPE_AUTHOR = 1
    const val TYPE_COMPANY = 0

    const val SHOW_CANT_UPDATE = 0
    const val SHOW_CANT_GET_LOCALLY = 1
    const val SHOW_CANT_GET_REMOTELY = 2

}

fun Boolean?.toInt(): Int = if (this != null && this) 1 else 0

enum class Status {
    SUCCESS,
    ERROR,
    LOADING
}

/**
 * Extension function to simplify setting an afterTextChanged action to EditText components.
 */
fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(editable: Editable?) {
            afterTextChanged.invoke(editable.toString())
        }

        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
    })
}