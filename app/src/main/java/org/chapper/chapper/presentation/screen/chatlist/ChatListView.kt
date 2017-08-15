package org.chapper.chapper.presentation.screen.chatlist

import com.arellomobile.mvp.MvpView

interface ChatListView : MvpView {
    fun initToolbar()
    fun initDrawer()
    fun initSQLTables()
    fun showDialogs()

    fun startSearchDevicesListActivity()
    fun shareWithFriends()
    fun startSettingsActivity()
    fun openFaqInBrowser()
    fun showError()
}