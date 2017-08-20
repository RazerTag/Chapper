package org.chapper.chapper.presentation.screen.devicelist

class DeviceListPresenter(private val viewState: DeviceListView) {
    fun init() {
        viewState.initToolbar()
        viewState.initDevices()
    }
}