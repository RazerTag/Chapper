package org.chapper.chapper.presentation.screen.chatlist

import com.arellomobile.mvp.MvpView

interface ChatListView : MvpView {
    fun initToolbar()
    fun initDrawer()
    fun initLoadingDrawer()
    fun initSQLTables()
    fun showDialogs()

    fun startSearchDevicesListActivity()
    fun shareWithFriends()
    fun startSettingsActivity()
    fun openFaqInBrowser()
    fun showError()

    fun btNotAvailable()
    fun btNotEnabled()
    fun btEnabled()
}