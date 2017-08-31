package org.chapper.chapper.data.bluetooth

import android.bluetooth.BluetoothAdapter
import android.content.Context
import app.akexorcist.bluetotohspp.library.BluetoothSPP
import kotlin.properties.Delegates

object BluetoothFactory {
    var sBtSPP: BluetoothSPP by Delegates.notNull()
    val sBtAdapter: BluetoothAdapter?
        get() = BluetoothAdapter.getDefaultAdapter()

    fun initBluetoothSSP(context: Context) {
        sBtSPP = BluetoothSPP(context)
    }
}