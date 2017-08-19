package org.chapper.chapper.presentation.screen.devicelist

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter

@InjectViewState
class DeviceListPresenter : MvpPresenter<DeviceListView>() {
    fun init() {
        viewState.initToolbar()
        viewState.initDevices()
    }
}