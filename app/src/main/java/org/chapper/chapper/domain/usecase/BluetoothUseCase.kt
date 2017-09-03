package org.chapper.chapper.domain.usecase

import android.bluetooth.BluetoothAdapter
import android.content.Context
import android.provider.Settings
import org.chapper.chapper.data.Constants
import org.chapper.chapper.data.bluetooth.BluetoothFactory
import org.chapper.chapper.data.bluetooth.BluetoothStatus
import org.chapper.chapper.data.repository.SettingsRepository

object BluetoothUseCase {
    fun startService() {
        val bt = BluetoothFactory.sBtSPP

        if (!bt.isBluetoothAvailable)
            return

        if (!bt.isBluetoothEnabled)
            return

        bt.setupService()
        bt.startService(true)
    }

    fun stopService() {
        val bt = BluetoothFactory.sBtSPP
        bt.stopService()
    }

    fun getBluetoothName(): String {
        return try {
            BluetoothAdapter.getDefaultAdapter().name
        } catch (e: Exception) {
            ""
        }
    }

    fun getBluetoothAddress(context: Context): String {
        return try {
            Settings.Secure.getString(context.contentResolver, "bluetooth_address")
        } catch (e: Exception) {
            ""
        }
    }

    fun setBluetoothName(name: String) {
        return try {
            BluetoothAdapter.getDefaultAdapter().name = "name"
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun checkStatus(): BluetoothStatus {
        val bt = BluetoothFactory.sBtSPP

        return if (bt.isBluetoothAvailable) {
            if (bt.isBluetoothEnabled) BluetoothStatus.ENABLED
            else BluetoothStatus.NOT_ENABLED
        } else BluetoothStatus.NOT_AVAILABLE
    }

    fun connect(address: String) {
        val bt = BluetoothFactory.sBtSPP

        if (bt.connectedDeviceAddress != address
                && checkStatus() == BluetoothStatus.ENABLED)
            try {
                bt.connect(address)
            } catch (e: Exception) {
                e.printStackTrace()
            }
    }

    fun send(text: String) {
        try {
            BluetoothFactory.sBtSPP.send(text, true)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun send(bytes: ByteArray) {
        try {
            BluetoothFactory.sBtSPP.send(bytes, true)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun shareUserData() {
        shareFirstName()
        shareLastName()
        sharePhoto()
    }

    private fun shareFirstName() {
        send(Constants.FIRST_NAME + SettingsRepository.getFirstName())
    }

    private fun shareLastName() {
        send(Constants.LAST_NAME + SettingsRepository.getLastName())
    }

    private fun sharePhoto() {
        // TODO : Do this method
    }

    fun isDiscovering(): Boolean {
        if (BluetoothFactory.sBtAdapter != null) {
            return BluetoothFactory.sBtAdapter!!.isDiscovering
        }
        return false
    }

    fun startDiscovery() {
        if (BluetoothFactory.sBtAdapter != null) {
            BluetoothFactory.sBtAdapter!!.startDiscovery()
        }
    }

    fun cancelDiscovery() {
        if (BluetoothFactory.sBtAdapter != null) {
            BluetoothFactory.sBtAdapter!!.cancelDiscovery()
        }
    }
}