package org.chapper.chapper.presentation.broadcastreceivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import org.chapper.chapper.presentation.screen.chatlist.ChatListPresenter

class BluetoothBroadcastReceiver(private val presenter: ChatListPresenter) : BroadcastReceiver() {
    override fun onReceive(p0: Context?, p1: Intent?) {
        presenter.bluetoothStatusAction()
    }
}