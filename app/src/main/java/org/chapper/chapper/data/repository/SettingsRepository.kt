package org.chapper.chapper.data.repository

import android.content.Context
import android.graphics.Bitmap
import com.raizlabs.android.dbflow.kotlinextensions.from
import com.raizlabs.android.dbflow.kotlinextensions.save
import com.raizlabs.android.dbflow.kotlinextensions.select
import org.chapper.chapper.data.model.Settings
import org.chapper.chapper.domain.usecase.BluetoothUseCase

object SettingsRepository {
    fun update() {
        val settings = (select from Settings::class).querySingle()!!
        settings.save()
    }

    fun getAddress(context: Context): String = BluetoothUseCase.getBluetoothAddress(context)

    fun getUsername(): String = BluetoothUseCase.getBluetoothName()

    fun getName(): String = "${getFirstName()} ${getLastName()}"

    fun getProfilePhoto(context: Context): Bitmap? = ImageRepository.getImage(context, "profile")

    fun isFirstStart(): Boolean = (select from Settings::class).querySingle()!!.isFirstStart

    fun getFirstName(): String = (select from Settings::class).querySingle()!!.firstName

    fun getLastName(): String = (select from Settings::class).querySingle()!!.lastName

    fun isSendByEnter(): Boolean = (select from Settings::class).querySingle()!!.isSendByEnter

    fun setUsername(username: String) {
        BluetoothUseCase.setBluetoothName(username)
    }

    fun setProfilePhoto(context: Context, image: Bitmap) {
        ImageRepository.saveImage(context, "profile", image)
    }

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

    fun setSendByEnter(sendByEnter: Boolean) {
        val settings = (select from Settings::class).querySingle()!!
        settings.isSendByEnter = sendByEnter
        settings.save()
    }
}