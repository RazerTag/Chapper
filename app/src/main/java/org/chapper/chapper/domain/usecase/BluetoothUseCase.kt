package org.chapper.chapper.domain.usecase

import android.bluetooth.BluetoothAdapter
import android.content.Context
import android.provider.Settings
import app.akexorcist.bluetotohspp.library.BluetoothState
import org.chapper.chapper.data.bluetooth.BluetoothFactory
import org.chapper.chapper.data.bluetooth.BluetoothStatus

object BluetoothUseCase {
    val bluetoothName: String
        get() {
            return if (BluetoothFactory.sBtAdapter != null) {
                BluetoothAdapter.getDefaultAdapter().name
            } else {
                ""
            }
        }

    fun getBluetoothAddress(context: Context): String {
        return if (BluetoothFactory.sBtAdapter != null) {
            Settings.Secure.getString(context.contentResolver, "bluetooth_address")
        } else {
            ""
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

    fun setupService() {
        if (checkStatus() == BluetoothStatus.ENABLED) {
            try {
                val bt = BluetoothFactory.sBtSPP
                bt.setupService()
                bt.startService(BluetoothState.DEVICE_ANDROID)
            } catch (e: Exception) {
                e.printStackTrace()
            }
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
}