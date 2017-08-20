package org.chapper.chapper.presentation.screen.devicelist

import android.content.Intent
import org.chapper.chapper.data.model.Device
import org.chapper.chapper.presentation.broadcastreceivers.BluetoothDiscoveryBroadcastReceiver

interface DeviceListView {
    fun initToolbar()
    fun addDevice(device: Device)

    fun initDevices()

    fun registerReceiver(listener: BluetoothDiscoveryBroadcastReceiver.ActionListener)

    fun deviceFound(intent: Intent)
    fun discoveryStarted(intent: Intent)
    fun discoveryFinished(intent: Intent)
}