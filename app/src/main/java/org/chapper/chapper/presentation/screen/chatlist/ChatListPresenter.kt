package org.chapper.chapper.presentation.screen.chatlist

import org.chapper.chapper.data.bluetooth.BluetoothStatus
import org.chapper.chapper.domain.usecase.BluetoothUsecase
import org.chapper.chapper.presentation.broadcastreceiver.BluetoothStateBroadcastReceiver

class ChatListPresenter(private val viewState: ChatListView) {
    fun init() {
        viewState.initToolbar()
        viewState.initLoadingDrawer()
        viewState.showDialogs()
    }

    fun handleDrawerItemClickListener(position: Int): Boolean {
        when (position) {
            1 -> {
                viewState.startSearchDevicesListActivity()
            }
            3 -> {
                viewState.shareWithFriends()
            }
            4 -> {
                viewState.startSettingsActivity()
            }
            5 -> {
                viewState.openFaqInBrowser()
            }
            else -> {
                viewState.showError()
            }
        }
        return false
    }

    fun bluetoothStatusAction() {
        val status = BluetoothUsecase().checkStatus()

        when (status) {
            BluetoothStatus.NOT_AVAILABLE -> viewState.btNotAvailable()
            BluetoothStatus.NOT_ENABLED -> viewState.btNotEnabled()
            BluetoothStatus.ENABLED -> viewState.btEnabled()
        }
    }

    fun registerReceiver() {
        val listener = object : BluetoothStateBroadcastReceiver.ActionListener {
            override fun onBluetoothStatusAction() {
                bluetoothStatusAction()
            }
        }

        viewState.registerReceiver(listener)
    }
}