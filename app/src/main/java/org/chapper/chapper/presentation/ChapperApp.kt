package org.chapper.chapper.presentation

import android.app.Application
import org.chapper.chapper.data.bluetooth.BluetoothFactory
import ru.arturvasilov.sqlite.core.SQLite

class ChapperApp : Application() {
    override fun onCreate() {
        super.onCreate()
        SQLite.initialize(this)
        BluetoothFactory.initBluetoothSSP(applicationContext)
    }
}