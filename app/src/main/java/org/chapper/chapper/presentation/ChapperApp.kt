package org.chapper.chapper.presentation

import android.app.Application
import app.akexorcist.bluetotohspp.library.BluetoothState
import com.raizlabs.android.dbflow.config.FlowManager
import com.raizlabs.android.dbflow.kotlinextensions.from
import com.raizlabs.android.dbflow.kotlinextensions.insert
import com.raizlabs.android.dbflow.kotlinextensions.select
import org.chapper.chapper.data.bluetooth.BluetoothFactory
import org.chapper.chapper.data.model.Settings

class ChapperApp : Application() {
    override fun onCreate() {
        super.onCreate()

        FlowManager.init(this)

        initSQLTables()

        BluetoothFactory.initBluetoothSSP(applicationContext)
        val mBt = BluetoothFactory.sBt
        mBt.setupService()
        mBt.startService(BluetoothState.DEVICE_ANDROID)
    }

    override fun onTerminate() {
        super.onTerminate()
        FlowManager.destroy()
    }

    private fun initSQLTables() {
        if (!(select from Settings::class).hasData()) {
            Settings().insert()
        }
    }
}