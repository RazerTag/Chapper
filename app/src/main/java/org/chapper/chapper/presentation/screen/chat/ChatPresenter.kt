package org.chapper.chapper.presentation.screen.chat

import org.chapper.chapper.data.bluetooth.BluetoothFactory

class ChatPresenter(private val viewState: ChatView) {
    fun init() {
        viewState.showMessages()
    }

    fun sendMessage(text: String) {
        BluetoothFactory.sBt.send(text, true)
    }
}