package org.chapper.chapper.presentation.broadcastreceiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import org.chapper.chapper.data.Constants

class BluetoothConnectionBroadcastReceiver(
        private val context: Context,
        private val listener: BluetoothConnectionBroadcastReceiver.ActionListener
) : BroadcastReceiver() {
    interface ActionListener {
        fun onDeviceConnected(name: String, address: String)
        fun onDeviceDisconnected()
        fun onDeviceConnectionFailed()
    }

    override fun onReceive(context: Context, intent: Intent) {
        when (intent.action) {
            Constants.ACTION_CONNECTED -> {
                listener.onDeviceConnected(
                        intent.getStringExtra(Constants.NAME_EXTRA),
                        intent.getStringExtra(Constants.ADDRESS_EXTRA)
                )
            }
            Constants.ACTION_DISCONNECTED -> {
                listener.onDeviceDisconnected()
            }
            Constants.ACTION_CONNECTION_FAILED -> {
                listener.onDeviceConnectionFailed()
            }
        }
    }

    fun registerContext() {
        var filter = IntentFilter(Constants.ACTION_CONNECTED)
        context.registerReceiver(this, filter)

        filter = IntentFilter(Constants.ACTION_DISCONNECTED)
        context.registerReceiver(this, filter)

        filter = IntentFilter(Constants.ACTION_CONNECTION_FAILED)
        context.registerReceiver(this, filter)
    }

    fun unregisterContext() {
        context.unregisterReceiver(this)
    }
}