package org.chapper.chapper.presentation.screen.chat

import com.raizlabs.android.dbflow.runtime.FlowContentObserver
import org.chapper.chapper.data.MessageStatus
import org.chapper.chapper.data.Values
import org.chapper.chapper.data.bluetooth.BluetoothFactory
import org.chapper.chapper.data.model.AppAction
import org.chapper.chapper.data.model.Chat
import org.chapper.chapper.data.model.Message
import org.chapper.chapper.data.repository.MessageRepository
import rx.Observable
import rx.schedulers.Schedulers

class ChatPresenter(private val viewState: ChatView) {
    fun init() {
        viewState.initChat()
        viewState.initToolbar()
        viewState.showMessages()
    }

    fun sendMessage(text: String) {
        if (text.isNotEmpty()) {
            val message = Message(chatId = viewState.getChatId(),
                    status = MessageStatus.OUTGOING_NOT_SENT,
                    text = text)
            Observable.just(text)
                    .doOnNext { message.insert() }
                    .doOnNext { BluetoothFactory.sBt.send(text, true) }
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
                .doOnNext { BluetoothFactory.sBt.send(Values.MESSAGES_READ, true) }
                .observeOn(Schedulers.newThread())
                .subscribe()
    }
}