package org.chapper.chapper.presentation.screen.chatlist

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter

@InjectViewState
class ChatListPresenter : MvpPresenter<ChatListView>() {
    fun init() {
        viewState.initSQLTables()
        viewState.initToolbar()
        viewState.initDrawer()
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
}