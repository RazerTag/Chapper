package org.chapper.chapper.presentation.screen.chatlist

import android.app.Activity
import android.content.Context
import android.content.Intent
import app.akexorcist.bluetotohspp.library.BluetoothSPP
import app.akexorcist.bluetotohspp.library.BluetoothState
import com.raizlabs.android.dbflow.kotlinextensions.insert
import org.chapper.chapper.R
import org.chapper.chapper.data.bluetooth.BluetoothFactory
import org.chapper.chapper.data.bluetooth.BluetoothStatus
import org.chapper.chapper.data.model.Chat
import org.chapper.chapper.data.model.Message
import org.chapper.chapper.domain.usecase.BluetoothUsecase
import org.chapper.chapper.presentation.broadcastreceiver.BluetoothStateBroadcastReceiver
import org.chapper.chapper.presentation.util.BluetoothHelper

class ChatListPresenter(private val viewState: ChatListView) {
    fun init() {
        viewState.initToolbar()
        viewState.initDrawer()
        viewState.showDialogs()
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
                    val name = data.getStringExtra(BluetoothState.DEVICE_NAME)
                    val address = data.getStringExtra(BluetoothState.EXTRA_DEVICE_ADDRESS)

                    BluetoothHelper.connect(address)
                    //addChat(name, address)
                }
            }
        }
    }

    fun bluetoothStatusAction() {
        val status = BluetoothUsecase().checkStatus()

        when (status) {
            BluetoothStatus.NOT_AVAILABLE -> viewState.btNotAvailable()
            BluetoothStatus.NOT_ENABLED -> viewState.btNotEnabled()
            BluetoothStatus.ENABLED -> viewState.btEnabled()
        }
    }

    fun registerBluetoothStateReceiver() {
        val listener = object : BluetoothStateBroadcastReceiver.ActionListener {
            override fun onBluetoothStatusAction() {
                bluetoothStatusAction()
            }
        }

        viewState.registerReceiver(listener)
    }

    fun addChat(context: Context, username: String, address: String) {
        val chat = Chat()
        chat.username = username
        chat.bluetoothMacAddress = address
        chat.firstName = "Loading..."
        val message = Message()
        message.text = context.getString(R.string.chat_created)
        chat.insert()
    }

    fun bluetoothConnectionListener() {
        BluetoothFactory.sBt.setBluetoothConnectionListener(object : BluetoothSPP.BluetoothConnectionListener {
            override fun onDeviceConnected(name: String?, address: String?) {
            }

            override fun onDeviceDisconnected() {
            }

            override fun onDeviceConnectionFailed() {
                viewState.showError()
            }
        })
    }

    fun onDataReceivedListener() {
        BluetoothFactory.sBt.setOnDataReceivedListener { data, message ->
            if (message != null) {

            } else if (data != null) {

            }
        }
    }
}