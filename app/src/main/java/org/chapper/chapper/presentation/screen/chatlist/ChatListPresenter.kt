package org.chapper.chapper.presentation.screen.chatlist

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import org.chapper.chapper.data.bluetooth.BluetoothStatus
import org.chapper.chapper.domain.usecase.BluetoothUsecase

@InjectViewState
class ChatListPresenter : MvpPresenter<ChatListView>() {
    fun init() {
        viewState.initSQLTables()
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
}