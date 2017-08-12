package org.chapper.chapper.data.bluetooth

import android.content.Context
import android.support.v4.content.AsyncTaskLoader
import app.akexorcist.bluetotohspp.library.BluetoothSPP

class BluetoothLoader(context: Context?) : AsyncTaskLoader<BluetoothSPP>(context) {
    override fun onStartLoading() {
        super.onStartLoading()
        forceLoad()
    }

    override fun loadInBackground(): BluetoothSPP {
        TODO("I'll do it later")
    }
}