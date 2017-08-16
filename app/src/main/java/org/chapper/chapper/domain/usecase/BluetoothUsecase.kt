package org.chapper.chapper.domain.usecase

import org.chapper.chapper.data.bluetooth.BluetoothFactory
import org.chapper.chapper.data.bluetooth.BluetoothStatus

class BluetoothUsecase {
    private val mBt = BluetoothFactory.sBt

    fun checkStatus(): BluetoothStatus {
        return if (mBt.isBluetoothAvailable) {
            if (mBt.isBluetoothEnabled) BluetoothStatus.ENABLED
            else BluetoothStatus.NOT_ENABLED
        } else BluetoothStatus.NOT_AVAILABLE
    }
}