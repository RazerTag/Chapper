package org.chapper.chapper.presentation.screen.chat

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import app.akexorcist.bluetotohspp.library.BluetoothSPP
import com.raizlabs.android.dbflow.kotlinextensions.save
import com.raizlabs.android.dbflow.runtime.FlowContentObserver
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.chapper.chapper.data.Constants
import org.chapper.chapper.data.bluetooth.BluetoothFactory
import org.chapper.chapper.data.model.Chat
import org.chapper.chapper.data.model.Message
import org.chapper.chapper.data.repository.ChatRepository
import org.chapper.chapper.data.repository.MessageRepository
import org.chapper.chapper.data.status.MessageStatus
import org.chapper.chapper.domain.usecase.BluetoothUseCase
import org.chapper.chapper.presentation.broadcastreceiver.BluetoothDiscoveryBroadcastReceiver
import java.util.*
import kotlin.properties.Delegates

class ChatPresenter(private val viewState: ChatView) {
    var mChatId: String by Delegates.notNull()
    var mChat: Chat by Delegates.notNull()

    var mReceiver: BroadcastReceiver? = null

    private val mTypingCalls: Queue<String> = ArrayDeque()

    var isTyping = false
    var isConnected = false
    var isNearby = false

    fun init(intent: Intent) {
        initChat(intent)
        viewState.initToolbar()
        viewState.showMessages()
        statusOffline()
    }

    private fun initChat(intent: Intent) {
        mChatId = intent.getStringExtra(Constants.CHAT_ID_EXTRA)
        mChat = ChatRepository.getChat(mChatId)

        BluetoothUseCase.connect(mChat.bluetoothMacAddress)
    }

    fun setupStatus(currentAddress: String) {
        if (BluetoothFactory.sBtSPP.connectedDeviceAddress == currentAddress) {
            statusConnected()
        } else {
            viewState.startRefreshing()
        }
    }

    private fun statusConnected() {
        viewState.statusConnected()
        hideRefresher()
        isConnected = true
        isNearby = true
    }

    private fun statusNearby() {
        viewState.statusNearby()
        showRefresher()
        isConnected = false
        isNearby = true
    }

    private fun statusOffline() {
        viewState.statusOffline()
        showRefresher()
        isConnected = false
        isNearby = false
    }

    private fun typing() {
        Observable.just("")
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext { startTyping() }
                .observeOn(Schedulers.newThread())
                .doOnNext { Thread.sleep(2500) }
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext { stopTyping() }
                .subscribe()
    }

    private fun startTyping() {
        isTyping = true
        mTypingCalls.add("")
        viewState.statusTyping()
    }

    private fun stopTyping() {
        isTyping = false
        mTypingCalls.poll()
        if (mTypingCalls.size == 0) {
            statusConnected()
        }
    }

    private fun showRefresher() {
        viewState.showRefresher()
    }

    private fun hideRefresher() {
        viewState.hideRefresher()
    }

    fun sendMessage(text: String) {
        if (text.isNotEmpty()) {
            val message = Message(chatId = mChatId,
                    status = MessageStatus.OUTGOING_NOT_SENT,
                    text = text)
            Observable.just(text)
                    .doOnNext { message.insert() }
                    .doOnNext { BluetoothUseCase.send(text) }
                    .observeOn(Schedulers.newThread())
                    .subscribe()
        }
    }

    fun databaseChangesListener(observer: FlowContentObserver) {
        observer.addModelChangeListener { table, _, _ ->
            when (table) {
                Message::class.java -> {
                    viewState.changeMessageList()
                    readMessages()
                    sendMessagesReadCode()
                }
            }
        }
    }

    fun readMessages() {
        Observable.just("")
                .doOnNext { MessageRepository.readIncomingMessages(mChatId) }
                .observeOn(Schedulers.newThread())
                .subscribe()
    }

    fun sendMessagesReadCode() {
        Observable.just("")
                .doOnNext { BluetoothUseCase.send(Constants.MESSAGES_READ) }
                .observeOn(Schedulers.newThread())
                .subscribe()
    }

    fun updateLastConnectionDate() {
        mChat.lastConnection = Date()
        mChat.save()
    }

    fun bluetoothConnectionListener() {
        BluetoothFactory.sBtSPP.setBluetoothConnectionListener(object : BluetoothSPP.BluetoothConnectionListener {
            override fun onDeviceConnected(name: String?, address: String?) {
                if (address == mChat.bluetoothMacAddress) {
                    statusConnected()
                    updateLastConnectionDate()
                }
            }

            override fun onDeviceDisconnected() {
                if (isConnected) {
                    statusOffline()
                    updateLastConnectionDate()
                    viewState.startRefreshing()
                }
            }

            override fun onDeviceConnectionFailed() {
                BluetoothUseCase.connect(mChat.bluetoothMacAddress)
            }
        })
    }

    fun startDiscovery(context: Context) {
        val listener = object : BluetoothDiscoveryBroadcastReceiver.ActionListener {
            override fun onDeviceFound(intent: Intent) {
                val device = intent.getParcelableExtra<BluetoothDevice>(BluetoothDevice.EXTRA_DEVICE)
                if (device.address == mChat.bluetoothMacAddress && !isConnected) {
                    statusNearby()
                    BluetoothUseCase.cancelDiscovery()
                }
            }

            override fun onDiscoveryStarted(intent: Intent) {
                if (!isConnected) {
                    statusOffline()
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

        BluetoothUseCase.startDiscovery()
    }

    fun unregisterReceiver(context: Context) {
        if (mReceiver != null)
            context.unregisterReceiver(mReceiver)
    }

    fun onDataReceivedListener() {
        BluetoothFactory.sBtSPP.setOnDataReceivedListener { _, message ->
            val name = BluetoothFactory.sBtSPP.connectedDeviceName
            val address = BluetoothFactory.sBtSPP.connectedDeviceAddress

            if (message != null && name != null && address != null) {
                if (address == mChat.bluetoothMacAddress) {
                    when (message) {
                        Constants.TYPING -> {
                            typing()
                        }
                    }
                }
            }
        }
    }
}