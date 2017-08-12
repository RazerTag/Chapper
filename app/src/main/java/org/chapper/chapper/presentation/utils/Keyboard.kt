package org.chapper.chapper.presentation.utils

import android.app.Activity
import android.view.inputmethod.InputMethodManager


object Keyboard {
    fun hideKeyboard(activity: Activity) {
        val inputMethodManager = activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(activity.currentFocus!!.windowToken, 0)
    }
}