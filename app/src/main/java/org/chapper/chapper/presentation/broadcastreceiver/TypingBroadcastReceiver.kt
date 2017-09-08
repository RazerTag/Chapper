package org.chapper.chapper.presentation.broadcastreceiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import org.chapper.chapper.data.Constants

class TypingBroadcastReceiver(private val context: Context, private val listener: ActionListener) : BroadcastReceiver() {
    interface ActionListener {
        fun onTyping()
    }

    override fun onReceive(context: Context, intent: Intent) {
        when (intent.action) {
            Constants.TYPING_TAG -> listener.onTyping()
        }
    }

    fun registerContext() {
        val filter = IntentFilter(Constants.TYPING_TAG)
        context.registerReceiver(this, filter)
    }

    fun unregisterContext() {
        context.unregisterReceiver(this)
    }
}