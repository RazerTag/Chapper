package org.chapper.chapper.presentation.broadcastreceivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class BluetoothStateBroadcastReceiver(private val listener: BluetoothStateBroadcastReceiver.ActionListener) : BroadcastReceiver() {
    interface ActionListener {
        fun onBluetoothStatusAction()
    }

    override fun onReceive(context: Context, intent: Intent) {
        listener.onBluetoothStatusAction()
    }
}