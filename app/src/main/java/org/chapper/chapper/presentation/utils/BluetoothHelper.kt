package org.chapper.chapper.presentation.utils

import android.bluetooth.BluetoothAdapter

object BluetoothHelper {
    val bluetoothAdapter get() = BluetoothAdapter.getDefaultAdapter()

    val bluetoothName: String
        get() {
            return if (bluetoothAdapter != null) {
                BluetoothAdapter.getDefaultAdapter().name
            } else {
                ""
            }
        }

    val bluetoothAddress: String
        get() {
            return if (bluetoothAdapter != null) {
                BluetoothAdapter.getDefaultAdapter().address
            } else {
                ""
            }
        }
}