package org.chapper.chapper.presentation.screen.chatlist

import android.app.Activity
import android.content.Context
import android.content.Intent
import com.raizlabs.android.dbflow.runtime.FlowContentObserver
import me.annenkov.bluekitten.BluetoothState
import org.chapper.chapper.data.bluetooth.BluetoothStatus
import org.chapper.chapper.data.model.Chat
import org.chapper.chapper.data.model.Message
import org.chapper.chapper.data.model.Settings
import org.chapper.chapper.domain.usecase.BluetoothUseCase
import org.chapper.chapper.presentation.broadcastreceiver.BluetoothStateBroadcastReceiver
import kotlin.properties.Delegates

class
ChatListPresenter(private val viewState: ChatListView) {
    private var mReceiver: BluetoothStateBroadcastReceiver by Delegates.notNull()

    fun init(context: Context) {
        viewState.initToolbar()
        viewState.initDrawer()
        viewState.showChats()
        registerTypingReceiver(context)
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
                    BluetoothUseCase.connect(address)
                }
            }
        }
    }

    fun bluetoothStatusAction() {
        val status = BluetoothUseCase.checkStatus()

        when (status) {
            BluetoothStatus.NOT_AVAILABLE -> viewState.btNotAvailable()
            BluetoothStatus.NOT_ENABLED -> viewState.btNotEnabled()
            BluetoothStatus.ENABLED -> viewState.btEnabled()
        }
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
            }
        }
    }

    private fun registerTypingReceiver(context: Context) {
        val listener = object : BluetoothStateBroadcastReceiver.ActionListener {
            override fun onBluetoothStatusAction() {
                viewState.changeChatList()
                bluetoothStatusAction()
            }
        }

        mReceiver = BluetoothStateBroadcastReceiver(context, listener)
        mReceiver.registerContext()
    }

    fun unregisterReceivers() {
        mReceiver.unregisterContext()
    }
}