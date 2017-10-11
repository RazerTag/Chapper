package org.chapper.chapper.presentation.screen.devicelist

import android.bluetooth.BluetoothDevice
import android.content.Context
import android.content.Intent
import org.chapper.chapper.data.model.Device
import org.chapper.chapper.data.repository.ChatRepository
import org.chapper.chapper.presentation.broadcastreceiver.BluetoothDiscoveryBroadcastReceiver
import kotlin.properties.Delegates

class DeviceListPresenter(private val viewState: DeviceListView) {
    var mBtDiscoveryReceiver: BluetoothDiscoveryBroadcastReceiver by Delegates.notNull()

    fun init() {
        viewState.initToolbar()
        viewState.initDevices()
    }

    fun deviceFound(intent: Intent) {
        val device = intent.getParcelableExtra<BluetoothDevice>(BluetoothDevice.EXTRA_DEVICE)

        val deviceModel = Device()

        if (device.name == null || device.name == "") return
        else deviceModel.bluetoothName = device.name
        deviceModel.bluetoothAddress = device.address

        if (!ChatRepository.contains(deviceModel.bluetoothAddress)) {
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

    fun registerReceiver(context: Context) {
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

        mBtDiscoveryReceiver = BluetoothDiscoveryBroadcastReceiver(context, listener)
        mBtDiscoveryReceiver.registerContext()
    }

    fun unregisterReceiver() {
        mBtDiscoveryReceiver.unregisterContext()
    }
}
