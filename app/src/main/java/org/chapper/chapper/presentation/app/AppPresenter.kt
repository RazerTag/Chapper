package org.chapper.chapper.presentation.app

import app.akexorcist.bluetotohspp.library.BluetoothSPP
import org.chapper.chapper.data.ActionType
import org.chapper.chapper.data.bluetooth.BluetoothFactory
import org.chapper.chapper.data.model.AppAction
import org.chapper.chapper.data.model.Message
import org.chapper.chapper.data.repository.ChatRepository
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
            if (message != null) {
                val id = ChatRepository.getChat(name, address).id
                Message(chatId = id, text = message)
            } else if (data != null) {
                // TODO : Do it later
            }
        }
    }

    fun bluetoothConnectionListener() {
        BluetoothFactory.sBt.setBluetoothConnectionListener(object : BluetoothSPP.BluetoothConnectionListener {
            override fun onDeviceConnected(name: String, address: String) {
            }

            override fun onDeviceDisconnected() {
            }

            override fun onDeviceConnectionFailed() {
                bluetoothStatusAction()
            }
        })
    }
}