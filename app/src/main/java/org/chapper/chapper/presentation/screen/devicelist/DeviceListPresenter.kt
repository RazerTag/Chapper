package org.chapper.chapper.presentation.screen.devicelist

import android.bluetooth.BluetoothDevice
import android.content.Intent
import org.chapper.chapper.data.model.Device
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
                discoveryStarted()
            }

            override fun onDiscoveryFinished(intent: Intent) {
                discoveryFinished()
            }
        }

        viewState.registerReceiver(listener)
    }

    fun deviceFound(intent: Intent) {
        val device = intent.getParcelableExtra<BluetoothDevice>(BluetoothDevice.EXTRA_DEVICE)

        if (device.bondState != BluetoothDevice.BOND_BONDED) {
            val deviceModel = Device()

            if (device.name == null || device.name == "") return
            else deviceModel.bluetoothName = device.name
            deviceModel.bluetoothAddress = device.address

            viewState.hideNoOneNearBlock()
            viewState.addDevice(deviceModel)
        }
    }

    fun discoveryStarted() {
        viewState.setRefreshingTitle()
        viewState.showNoOneNearBlock()
    }

    fun discoveryFinished() {
        viewState.setSectionNameTitle()
    }
}