package org.chapper.chapper.presentation.app

import org.chapper.chapper.presentation.broadcastreceiver.BluetoothStateBroadcastReceiver

interface AppView {
    fun registerReceiver(listener: BluetoothStateBroadcastReceiver.ActionListener)
}