package org.chapper.chapper.presentation.screen.chat

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import app.akexorcist.bluetotohspp.library.BluetoothSPP
import com.raizlabs.android.dbflow.runtime.FlowContentObserver
import org.chapper.chapper.data.Constants
import org.chapper.chapper.data.MessageStatus
import org.chapper.chapper.data.bluetooth.BluetoothFactory
import org.chapper.chapper.data.model.AppAction
import org.chapper.chapper.data.model.Chat
import org.chapper.chapper.data.model.Message
import org.chapper.chapper.data.repository.MessageRepository
import org.chapper.chapper.domain.usecase.BluetoothUsecase
import org.chapper.chapper.presentation.broadcastreceiver.BluetoothDiscoveryBroadcastReceiver
import rx.Observable
import rx.schedulers.Schedulers

class ChatPresenter(private val viewState: ChatView) {
    private var mReceiver: BroadcastReceiver? = null
    var isConnected = false
    var isNearby = false

    fun init() {
        viewState.initChat()
        viewState.initToolbar()
        viewState.showMessages()
    }

    fun setupStatus(currentAddress: String) {
        if (BluetoothFactory.sBt.connectedDeviceAddress == currentAddress) {
            statusConnected()
        } else {
            viewState.startRefreshing()
        }
    }

    private fun statusConnected() {
        viewState.statusConnected()
        isConnected = true
        isNearby = true
    }

    private fun statusNearby() {
        viewState.statusNearby()
        isConnected = false
        isNearby = true
    }

    private fun statusOffline() {
        viewState.statusOffline()
        isConnected = false
        isNearby = false
    }

    private fun statusRefreshing() {
        viewState.statusRefresing()
        isConnected = false
        isNearby = false
    }

    fun sendMessage(text: String) {
        if (text.isNotEmpty()) {
            val message = Message(chatId = viewState.getChatId(),
                    status = MessageStatus.OUTGOING_NOT_SENT,
                    text = text)
            Observable.just(text)
                    .doOnNext { message.insert() }
                    .doOnNext { BluetoothUsecase.send(text) }
                    .observeOn(Schedulers.newThread())
                    .subscribe()
        }
    }

    fun databaseChangesListener(observer: FlowContentObserver) {
        observer.addModelChangeListener { table, _, _ ->
            when (table) {
                Chat::class.java -> {
                    //viewState.changeChatList()
                }
                Message::class.java -> {
                    viewState.changeMessageList()
                    readMessages()
                    sendMessagesReadCode()
                }
                AppAction::class.java -> {
                    //bluetoothStatusAction()
                }
            }
        }
    }

    fun readMessages() {
        Observable.just("")
                .doOnNext { MessageRepository.readIncomingMessages(viewState.getChatId()) }
                .observeOn(Schedulers.newThread())
                .subscribe()
    }

    fun sendMessagesReadCode() {
        Observable.just("")
                .doOnNext { BluetoothUsecase.send(Constants.MESSAGES_READ) }
                .observeOn(Schedulers.newThread())
                .subscribe()
    }

    fun bluetoothConnectionListener(currentAddress: String) {
        BluetoothFactory.sBt.setBluetoothConnectionListener(object : BluetoothSPP.BluetoothConnectionListener {
            override fun onDeviceConnected(name: String, address: String) {
                if (address == currentAddress) {
                    statusConnected()
                }
            }

            override fun onDeviceDisconnected() {
                if (isConnected) {
                    statusOffline()
                    viewState.startRefreshing()
                }
            }

            override fun onDeviceConnectionFailed() {

            }
        })
    }

    fun startDiscovery(context: Context, currentAddress: String) {
        val listener = object : BluetoothDiscoveryBroadcastReceiver.ActionListener {
            override fun onDeviceFound(intent: Intent) {
                val device = intent.getParcelableExtra<BluetoothDevice>(BluetoothDevice.EXTRA_DEVICE)
                if (device.address == currentAddress && !isConnected) {
                    statusNearby()
                    BluetoothFactory.sBt.cancelDiscovery()
                }
            }

            override fun onDiscoveryStarted(intent: Intent) {
                if (!isConnected) {
                    statusRefreshing()
                }
            }

            override fun onDiscoveryFinished(intent: Intent) {
                if (!isNearby && !isConnected) {
                    statusOffline()
                }
            }
        }

        mReceiver = BluetoothDiscoveryBroadcastReceiver(listener)

        var filter = IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_STARTED)
        context.registerReceiver(mReceiver, filter)

        filter = IntentFilter(BluetoothDevice.ACTION_FOUND)
        context.registerReceiver(mReceiver, filter)

        filter = IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)
        context.registerReceiver(mReceiver, filter)

        BluetoothFactory.sBt.startDiscovery()
    }

    fun unregisterReceiver(context: Context) {
        if (mReceiver != null)
            context.unregisterReceiver(mReceiver)
    }
}