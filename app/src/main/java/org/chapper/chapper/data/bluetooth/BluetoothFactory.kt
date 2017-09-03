package org.chapper.chapper.data.bluetooth

import android.bluetooth.BluetoothAdapter
import android.content.Context
import app.akexorcist.bluetotohspp.library.BluetoothSPP
import com.polidea.rxandroidble.RxBleClient
import com.polidea.rxandroidble.RxBleDevice
import kotlin.properties.Delegates

object BluetoothFactory {
    var sBtSPP: BluetoothSPP by Delegates.notNull()

    var sBle: RxBleClient by Delegates.notNull()
    var sBleDevice: RxBleDevice? = null

    val sBtAdapter: BluetoothAdapter?
        get() = BluetoothAdapter.getDefaultAdapter()

    fun initBluetoothSSP(context: Context) {
        sBtSPP = BluetoothSPP(context)
    }
}