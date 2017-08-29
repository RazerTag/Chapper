package org.chapper.chapper.presentation.util

import android.bluetooth.BluetoothAdapter
import android.content.Context
import android.provider.Settings.Secure

object BluetoothHelper {
    val bluetoothAdapter: BluetoothAdapter? get() = BluetoothAdapter.getDefaultAdapter()

    val bluetoothName: String
        get() {
            return if (bluetoothAdapter != null) {
                BluetoothAdapter.getDefaultAdapter().name
            } else {
                ""
            }
        }

    fun getBluetoothAddress(context: Context): String {
        return if (bluetoothAdapter != null) {
            Secure.getString(context.contentResolver, "bluetooth_address")
        } else {
            ""
        }
    }
}