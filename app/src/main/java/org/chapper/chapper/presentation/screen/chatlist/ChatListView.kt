package org.chapper.chapper.presentation.screen.chatlist

import org.chapper.chapper.data.model.Chat

interface ChatListView {
    fun initToolbar()
    fun initDrawer()
    fun initChats()
    fun changeChatList()
    fun showNoChats(chats: MutableList<Chat>)

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