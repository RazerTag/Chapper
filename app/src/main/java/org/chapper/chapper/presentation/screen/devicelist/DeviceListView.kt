package org.chapper.chapper.presentation.screen.devicelist

import org.chapper.chapper.data.model.Device
import org.chapper.chapper.presentation.broadcastreceiver.BluetoothDiscoveryBroadcastReceiver

interface DeviceListView {
    fun initToolbar()
    fun addDevice(device: Device)

    fun initDevices()

    fun registerReceiver(listener: BluetoothDiscoveryBroadcastReceiver.ActionListener)

    fun showNoOneNearBlock()
    fun hideNoOneNearBlock()

    fun setRefreshingTitle()
    fun setSectionNameTitle()
}