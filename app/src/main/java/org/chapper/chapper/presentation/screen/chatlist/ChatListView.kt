package org.chapper.chapper.presentation.screen.chatlist

interface ChatListView {
    fun initToolbar()
    fun initDrawer()
    fun showChats()
    fun changeChatList()

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
}