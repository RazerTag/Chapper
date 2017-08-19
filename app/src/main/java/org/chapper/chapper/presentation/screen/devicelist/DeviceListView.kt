package org.chapper.chapper.presentation.screen.devicelist

import com.arellomobile.mvp.MvpView
import org.chapper.chapper.data.model.Device

interface DeviceListView : MvpView {
    fun initToolbar()
    fun addDevice(device: Device)

    fun initDevices()
}