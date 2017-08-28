package org.chapper.chapper.presentation.app

import android.content.Context
import app.akexorcist.bluetotohspp.library.BluetoothSPP
import com.raizlabs.android.dbflow.kotlinextensions.insert
import org.chapper.chapper.R
import org.chapper.chapper.data.ActionType
import org.chapper.chapper.data.MessageStatus
import org.chapper.chapper.data.Values
import org.chapper.chapper.data.bluetooth.BluetoothFactory
import org.chapper.chapper.data.model.AppAction
import org.chapper.chapper.data.model.Chat
import org.chapper.chapper.data.model.Message
import org.chapper.chapper.data.repository.ChatRepository
import org.chapper.chapper.data.repository.MessageRepository
import org.chapper.chapper.presentation.broadcastreceiver.BluetoothStateBroadcastReceiver

class AppPresenter(private val viewState: AppView) {
    fun bluetoothStatusAction() {
        AppAction(type = ActionType.BLUETOOTH_ACTION).insert()
    }

    fun registerBluetoothStateReceiver() {
        val listener = object : BluetoothStateBroadcastReceiver.ActionListener {
            override fun onBluetoothStatusAction() {
                bluetoothStatusAction()
            }
        }

        viewState.registerReceiver(listener)
    }

    fun onDataReceivedListener() {
        BluetoothFactory.sBt.setOnDataReceivedListener { data, message ->
            val name = BluetoothFactory.sBt.connectedDeviceName
            val address = BluetoothFactory.sBt.connectedDeviceAddress
            val id = ChatRepository.getChat(name, address).id
            if (message != null) {
                when (message) {
                    Values.MESSAGES_READ -> MessageRepository.readOutgoingMessages(id)

                    Values.MESSAGE_RECEIVED -> MessageRepository.receiveMessages(id)

                    else -> {
                        Message(chatId = id, text = message).insert()
                        BluetoothFactory.sBt.send(Values.MESSAGE_RECEIVED, true)
                    }
                }
            } else if (data != null) {
                // TODO : Do it later
            }
        }
    }

    fun bluetoothConnectionListener(context: Context) {
        BluetoothFactory.sBt.setBluetoothConnectionListener(object : BluetoothSPP.BluetoothConnectionListener {
            override fun onDeviceConnected(name: String, address: String) {
                addChat(context, name, address)
            }

            override fun onDeviceDisconnected() {
            }

            override fun onDeviceConnectionFailed() {
                bluetoothStatusAction()
            }
        })
    }

    fun addChat(context: Context, username: String, address: String) {
        if (!ChatRepository.contains(address)) {
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
    }
}