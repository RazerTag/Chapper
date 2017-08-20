package org.chapper.chapper.presentation.screen.chatlist

import org.chapper.chapper.presentation.broadcastreceiver.BluetoothStateBroadcastReceiver

interface ChatListView {
    fun initToolbar()
    fun initDrawer()
    fun initLoadingDrawer()
    fun showDialogs()

    fun startSearchDevicesListActivity()
    fun shareWithFriends()
    fun startSettingsActivity()
    fun openFaqInBrowser()
    fun showError()

    fun btNotAvailable()
    fun btNotEnabled()
    fun btEnabled()

    fun registerReceiver(listener: BluetoothStateBroadcastReceiver.ActionListener)
}