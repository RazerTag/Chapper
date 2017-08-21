package org.chapper.chapper.presentation.screen.chatlist

import org.chapper.chapper.presentation.broadcastreceiver.BluetoothStateBroadcastReceiver

interface ChatListView {
    fun initToolbar()
    fun initDrawer()
    fun showDialogs()
    fun initFlowManager()

    fun startSearchDevicesListActivity()
    fun startEnableBluetoothDiscoverableActivity()
    fun shareWithFriends()
    fun startSettingsActivity()
    fun openFaqInBrowser()
    fun showError()

    fun btNotAvailable()
    fun btNotEnabled()
    fun btEnabled()

    fun showToast(text: String)

    fun registerReceiver(listener: BluetoothStateBroadcastReceiver.ActionListener)
}