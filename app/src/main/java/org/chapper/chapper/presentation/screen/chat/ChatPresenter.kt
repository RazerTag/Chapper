package org.chapper.chapper.presentation.screen.chat

import android.bluetooth.BluetoothDevice
import android.content.Context
import android.content.Intent
import com.raizlabs.android.dbflow.kotlinextensions.save
import com.raizlabs.android.dbflow.runtime.FlowContentObserver
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import me.annenkov.bluekitten.BluetoothSPP
import org.chapper.chapper.data.Constants
import org.chapper.chapper.data.bluetooth.BluetoothFactory
import org.chapper.chapper.data.model.Chat
import org.chapper.chapper.data.model.Message
import org.chapper.chapper.data.repository.ChatRepository
import org.chapper.chapper.data.repository.MessageRepository
import org.chapper.chapper.data.status.MessageStatus
import org.chapper.chapper.domain.usecase.BluetoothUseCase
import org.chapper.chapper.presentation.broadcastreceiver.BluetoothDiscoveryBroadcastReceiver
import org.chapper.chapper.presentation.broadcastreceiver.TypingBroadcastReceiver
import java.util.*
import kotlin.properties.Delegates

class ChatPresenter(private val viewState: ChatView) {
    var mChatId: String by Delegates.notNull()
    var mChat: Chat by Delegates.notNull()

    private var isTyping = false
    private var isConnected = false
    private var isNearby = false

    private val mTypingCalls: Queue<String> = ArrayDeque()

    private var mBtDiscoveryReceiver: BluetoothDiscoveryBroadcastReceiver by Delegates.notNull()
    private var mTypingReceiver: TypingBroadcastReceiver by Delegates.notNull()

    private var mFlowObserver: FlowContentObserver by Delegates.notNull()

    fun init(context: Context, intent: Intent) {
        initChat(intent)
        viewState.initToolbar()
        viewState.showMessages()

        statusOffline()

        registerReceivers(context)
        setupStatus(mChat.bluetoothMacAddress)
        bluetoothConnectionListener()

        readMessages()
        sendMessagesReadCode()
    }

    fun destroy(context: Context) {
        unregisterReceivers(context)
    }

    private fun initChat(intent: Intent) {
        mChatId = intent.getStringExtra(Constants.CHAT_ID_EXTRA)
        mChat = ChatRepository.getChat(mChatId)

        BluetoothUseCase.connect(mChat.bluetoothMacAddress)
    }

    private fun setupStatus(currentAddress: String) {
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

    private fun databaseChangesListener(observer: FlowContentObserver) {
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

    private fun readMessages() {
        Observable.just("")
                .doOnNext { MessageRepository.readIncomingMessages(mChatId) }
                .observeOn(Schedulers.newThread())
                .subscribe()
    }

    private fun sendMessagesReadCode() {
        Observable.just("")
                .doOnNext { BluetoothUseCase.send(Constants.MESSAGES_READ) }
                .observeOn(Schedulers.newThread())
                .subscribe()
    }

    fun updateLastConnectionDate() {
        mChat.lastConnection = Date()
        mChat.save()
    }

    private fun bluetoothConnectionListener() {
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
                Observable.just("")
                        .observeOn(Schedulers.newThread())
                        .doOnNext { Thread.sleep(2500) }
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnNext { BluetoothUseCase.connect(mChat.bluetoothMacAddress) }
                        .subscribe()
            }
        })
    }

    fun startDiscovery() {
        BluetoothUseCase.startDiscovery()
    }

    private fun registerReceivers(context: Context) {
        registerFlowObserver(context)
        registerDiscoveryReceiver(context)
        registerTypingReceiver(context)
    }

    fun unregisterReceivers(context: Context) {
        mFlowObserver.unregisterForContentChanges(context)
        mBtDiscoveryReceiver.unregisterContext()
        mTypingReceiver.unregisterContext()
    }

    private fun registerFlowObserver(context: Context) {
        mFlowObserver = FlowContentObserver()
        mFlowObserver.registerForContentChanges(context, Chat::class.java)
        mFlowObserver.registerForContentChanges(context, Message::class.java)
        databaseChangesListener(mFlowObserver)
    }

    private fun registerDiscoveryReceiver(context: Context) {
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

        mBtDiscoveryReceiver = BluetoothDiscoveryBroadcastReceiver(context, listener)
        mBtDiscoveryReceiver.registerContext()
    }

    private fun registerTypingReceiver(context: Context) {
        val listener = object : TypingBroadcastReceiver.ActionListener {
            override fun onTyping() {
                typing()
            }
        }

        mTypingReceiver = TypingBroadcastReceiver(context, listener)
        mTypingReceiver.registerContext()
    }
}