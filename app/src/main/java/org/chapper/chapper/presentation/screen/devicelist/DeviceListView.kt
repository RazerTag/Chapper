package org.chapper.chapper.presentation.screen.devicelist

import org.chapper.chapper.data.model.Device

interface DeviceListView {
    fun initToolbar()
    fun addDevice(device: Device)

    fun initDevices()

    fun showNoOneNearBlock()
    fun hideNoOneNearBlock()

    fun setRefreshingTitle()
    fun setSectionNameTitle()

    fun isCoarseLocationPermissionDenied(): Boolean
    fun requestCoarseLocationPermission()
}