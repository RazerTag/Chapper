package org.chapper.chapper.bluetooth

import android.content.Context
import app.akexorcist.bluetotohspp.library.BluetoothSPP

object BluetoothFactory {
    private var sBt: BluetoothSPP? = null

    fun getBluetoothSSP(context: Context): BluetoothSPP {
        var bt = sBt
        if (bt == null) {
            synchronized(BluetoothFactory) {
                bt = sBt
                if (bt == null) {
                    bt = BluetoothSPP(context)
                }
            }
        }
        return bt!!
    }
}