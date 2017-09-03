package org.chapper.chapper.presentation.screen.settings

import android.graphics.Bitmap

interface SettingsView {
    fun initToolbar()
    fun initPhoto(bitmap: Bitmap)
    fun initName(text: String)
    fun initAddress(text: String)
    fun initUsername(text: String)
}