package org.chapper.chapper.presentation.app

import android.content.Context
import app.akexorcist.bluetotohspp.library.BluetoothSPP
import com.raizlabs.android.dbflow.kotlinextensions.insert
import com.raizlabs.android.dbflow.kotlinextensions.save
import org.chapper.chapper.R
import org.chapper.chapper.data.ActionType
import org.chapper.chapper.data.Constants
import org.chapper.chapper.data.MessageStatus
import org.chapper.chapper.data.bluetooth.BluetoothFactory
import org.chapper.chapper.data.bluetooth.BluetoothStatus
import org.chapper.chapper.data.model.AppAction
import org.chapper.chapper.data.model.Chat
import org.chapper.chapper.data.model.Message
import org.chapper.chapper.data.repository.ChatRepository
import org.chapper.chapper.data.repository.MessageRepository
import org.chapper.chapper.domain.usecase.BluetoothUseCase
import org.chapper.chapper.presentation.broadcastreceiver.BluetoothStateBroadcastReceiver

class AppPresenter(private val viewState: AppView) {
    fun bluetoothStatusAction() {
        AppAction(type = ActionType.BLUETOOTH_ACTION).insert()

        if (BluetoothUseCase.checkStatus() == BluetoothStatus.ENABLED) {
            BluetoothUseCase.startService()
        } else {
            BluetoothUseCase.stopService()
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

    fun onDataReceivedListener() {
        BluetoothFactory.sBtSPP.setOnDataReceivedListener { _, message ->
            val name = BluetoothFactory.sBtSPP.connectedDeviceName
            val address = BluetoothFactory.sBtSPP.connectedDeviceAddress

            if (message != null && name != null && address != null) {
                val chat = ChatRepository.getChat(name, address)
                val id = chat.id

                when {
                    message == Constants.MESSAGES_READ -> MessageRepository.readOutgoingMessages(id)

                    message == Constants.MESSAGE_RECEIVED -> MessageRepository.receiveMessages(id)

                    message.contains(Constants.FIRST_NAME) -> {
                        val text = message.replace(Constants.FIRST_NAME, "")
                        if (text.isNotEmpty()) {
                            chat.firstName = text
                            chat.save()
                        }
                    }

                    message.contains(Constants.LAST_NAME) -> {
                        val text = message.replace(Constants.LAST_NAME, "")
                        if (text.isNotEmpty()) {
                            chat.lastName = text
                            chat.save()
                        }
                    }

                    else -> {
                        Message(chatId = id, text = message).insert()
                        BluetoothUseCase.send(Constants.MESSAGE_RECEIVED)
                    }
                }
            }
        }
    }

    fun bluetoothConnectionListener(context: Context) {
        BluetoothFactory.sBtSPP.setBluetoothConnectionListener(object : BluetoothSPP.BluetoothConnectionListener {
            override fun onDeviceConnected(name: String?, address: String?) {
                if (name != null && address != null) {
                    if (!ChatRepository.contains(address)) {
                        addChat(context, name, address)
                        BluetoothUseCase.shareUserData()
                    }
                }
            }

            override fun onDeviceDisconnected() {
            }

            override fun onDeviceConnectionFailed() {
                bluetoothStatusAction()
            }
        })
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
}