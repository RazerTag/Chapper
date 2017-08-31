package org.chapper.chapper.presentation.screen.devicelist

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import org.chapper.chapper.data.model.Device
import org.chapper.chapper.presentation.broadcastreceiver.BluetoothDiscoveryBroadcastReceiver

class DeviceListPresenter(private val viewState: DeviceListView) {
    var mReceiver: BroadcastReceiver? = null

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

        viewState.hideNoOneNearBlock()
        viewState.addDevice(deviceModel)
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

        mReceiver = BluetoothDiscoveryBroadcastReceiver(listener)

        var filter = IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_STARTED)
        context.registerReceiver(mReceiver, filter)

        filter = IntentFilter(BluetoothDevice.ACTION_FOUND)
        context.registerReceiver(mReceiver, filter)

        filter = IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)
        context.registerReceiver(mReceiver, filter)
    }

    fun unregisterReceiver(context: Context) {
        fun unregisterReceiver(context: Context) {
            if (mReceiver != null)
                context.unregisterReceiver(mReceiver)
        }
    }
}
