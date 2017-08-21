package org.chapper.chapper.presentation

import android.app.Application
import app.akexorcist.bluetotohspp.library.BluetoothState
import org.chapper.chapper.data.bluetooth.BluetoothFactory
import org.chapper.chapper.data.model.Settings
import org.chapper.chapper.data.table.SettingsTable
import ru.arturvasilov.sqlite.core.SQLite

class ChapperApp : Application() {
    override fun onCreate() {
        super.onCreate()

        SQLite.initialize(this)
        initSQLTables()

        BluetoothFactory.initBluetoothSSP(applicationContext)
        val mBt = BluetoothFactory.sBt
        mBt.setupService()
        mBt.startService(BluetoothState.DEVICE_ANDROID)
    }

    private fun initSQLTables() {
        if (SQLite.get().query(SettingsTable.TABLE).isEmpty()) {
            val settings = Settings()
            settings.isFirstStart = true
            settings.firstName = ""
            settings.lastName = ""
            SQLite.get().insert(SettingsTable.TABLE, settings)
        }
    }


}