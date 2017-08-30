package org.chapper.chapper.presentation.screen.chatlist

import android.app.Activity
import android.content.Context
import android.content.Intent
import app.akexorcist.bluetotohspp.library.BluetoothState
import com.raizlabs.android.dbflow.kotlinextensions.insert
import com.raizlabs.android.dbflow.runtime.FlowContentObserver
import org.chapper.chapper.R
import org.chapper.chapper.data.MessageStatus
import org.chapper.chapper.data.bluetooth.BluetoothStatus
import org.chapper.chapper.data.model.AppAction
import org.chapper.chapper.data.model.Chat
import org.chapper.chapper.data.model.Message
import org.chapper.chapper.data.model.Settings
import org.chapper.chapper.domain.usecase.BluetoothUsecase

class
ChatListPresenter(private val viewState: ChatListView) {
    fun init() {
        viewState.initToolbar()
        viewState.initDrawer()
        viewState.showChats()
    }

    fun handleDrawerItemClickListener(position: Int): Boolean {
        when (position) {
            1 -> {
                viewState.startSearchDevicesListActivity()
            }
            2 -> {
                viewState.startEnableBluetoothDiscoverableActivity()
            }
            4 -> {
                viewState.shareWithFriends()
            }
            5 -> {
                viewState.startSettingsActivity()
            }
            6 -> {
                viewState.openFaqInBrowser()
            }
            else -> {
                viewState.showError()
            }
        }
        return false
    }

    fun activityResult(requestCode: Int, resultCode: Int, data: Intent) {
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                BluetoothState.REQUEST_CONNECT_DEVICE -> {
                    val address = data.getStringExtra(BluetoothState.EXTRA_DEVICE_ADDRESS)
                    BluetoothUsecase.connect(address)
                }
            }
        }
    }

    fun bluetoothStatusAction() {
        val status = BluetoothUsecase.checkStatus()

        when (status) {
            BluetoothStatus.NOT_AVAILABLE -> viewState.btNotAvailable()
            BluetoothStatus.NOT_ENABLED -> viewState.btNotEnabled()
            BluetoothStatus.ENABLED -> viewState.btEnabled()
        }
    }

    fun addChat(context: Context, username: String, address: String) {
        val chat = Chat()
        chat.username = username
        chat.bluetoothMacAddress = address
        chat.firstName = context.getString(R.string.loading)
        chat.insert()
        Message(chatId = chat.id,
                text = context.getString(R.string.chat_created),
                status = MessageStatus.ACTION)
                .insert()
    }

    fun databaseChangesListener(observer: FlowContentObserver) {
        observer.addModelChangeListener { table, _, _ ->
            when (table) {
                Settings::class.java -> {
                    viewState.initDrawer()
                }
                Chat::class.java -> {
                    viewState.changeChatList()
                }
                Message::class.java -> {
                    viewState.changeChatList()
                }
                AppAction::class.java -> {
                    viewState.changeChatList()
                    bluetoothStatusAction()
                }
            }
        }
    }
}