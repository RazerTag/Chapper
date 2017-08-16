package org.chapper.chapper.data.bluetooth

import android.content.Context
import app.akexorcist.bluetotohspp.library.BluetoothSPP
import kotlin.properties.Delegates

object BluetoothFactory {
    var sBt: BluetoothSPP by Delegates.notNull()

    fun initBluetoothSSP(context: Context) {
        sBt = BluetoothSPP(context)
    }
}