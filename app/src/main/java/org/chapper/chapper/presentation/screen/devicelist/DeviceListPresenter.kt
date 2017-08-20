package org.chapper.chapper.presentation.screen.devicelist

import android.content.Intent
import org.chapper.chapper.presentation.broadcastreceiver.BluetoothDiscoveryBroadcastReceiver

class DeviceListPresenter(private val viewState: DeviceListView) {
    fun init() {
        viewState.initToolbar()
        viewState.initDevices()
    }

    fun registerReceiver() {
        val listener = object : BluetoothDiscoveryBroadcastReceiver.ActionListener {
            override fun onDeviceFound(intent: Intent) {
                deviceFound(intent)
            }

            override fun onDiscoveryStarted(intent: Intent) {
                discoveryStarted(intent)
            }

            override fun onDiscoveryFinished(intent: Intent) {
                discoveryFinished(intent)
            }
        }

        viewState.registerReceiver(listener)
    }

    fun deviceFound(intent: Intent) {
        viewState.deviceFound(intent)
    }

    fun discoveryStarted(intent: Intent) {
        viewState.discoveryStarted(intent)
    }

    fun discoveryFinished(intent: Intent) {
        viewState.discoveryFinished(intent)
    }
}