package org.chapper.chapper.data.repository

import com.raizlabs.android.dbflow.kotlinextensions.from
import com.raizlabs.android.dbflow.kotlinextensions.save
import com.raizlabs.android.dbflow.kotlinextensions.select
import org.chapper.chapper.data.model.Settings

object SettingsRepository {
    fun isFirstStart(): Boolean = (select from Settings::class).querySingle()!!.isFirstStart

    fun getFirstName(): String = (select from Settings::class).querySingle()!!.firstName

    fun getLastName(): String = (select from Settings::class).querySingle()!!.lastName

    fun setFirstStart(isFirstStart: Boolean) {
        val settings = (select from Settings::class).querySingle()!!
        settings.isFirstStart = isFirstStart
        settings.save()
    }

    fun setFirstName(firstName: String) {
        val settings = (select from Settings::class).querySingle()!!
        settings.firstName = firstName
        settings.save()
    }

    fun setLastName(lastName: String) {
        val settings = (select from Settings::class).querySingle()!!
        settings.lastName = lastName
        settings.save()
    }
}