package org.chapper.chapper.domain.usecase

import app.akexorcist.bluetotohspp.library.BluetoothState
import org.chapper.chapper.data.bluetooth.BluetoothFactory
import org.chapper.chapper.data.bluetooth.BluetoothStatus

object BluetoothUsecase {
    fun checkStatus(): BluetoothStatus {
        val bt = BluetoothFactory.sBt

        return if (bt.isBluetoothAvailable) {
            if (bt.isBluetoothEnabled) BluetoothStatus.ENABLED
            else BluetoothStatus.NOT_ENABLED
        } else BluetoothStatus.NOT_AVAILABLE
    }

    fun connect(address: String) {
        val bt = BluetoothFactory.sBt

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
                val bt = BluetoothFactory.sBt
                bt.setupService()
                bt.startService(BluetoothState.DEVICE_ANDROID)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun send(text: String) {
        try {
            BluetoothFactory.sBt.send(text, true)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun send(bytes: ByteArray) {
        try {
            BluetoothFactory.sBt.send(bytes, true)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}