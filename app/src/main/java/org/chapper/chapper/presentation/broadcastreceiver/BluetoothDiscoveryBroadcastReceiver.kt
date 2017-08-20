package org.chapper.chapper.presentation.broadcastreceiver

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class BluetoothDiscoveryBroadcastReceiver(private val listener: ActionListener) : BroadcastReceiver() {
    interface ActionListener {
        fun onDeviceFound(intent: Intent)
        fun onDiscoveryStarted(intent: Intent)
        fun onDiscoveryFinished(intent: Intent)
    }

    override fun onReceive(context: Context, intent: Intent) {
        when (intent.action) {
            BluetoothDevice.ACTION_FOUND -> {
                listener.onDeviceFound(intent)
            }
            BluetoothAdapter.ACTION_DISCOVERY_STARTED -> {
                listener.onDiscoveryStarted(intent)
            }
            BluetoothAdapter.ACTION_DISCOVERY_FINISHED -> {
                listener.onDiscoveryFinished(intent)
            }
        }
    }
}