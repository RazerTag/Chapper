package org.chapper.chapper.presentation

import android.app.Application
import android.content.Intent
import com.raizlabs.android.dbflow.config.FlowManager
import com.raizlabs.android.dbflow.kotlinextensions.from
import com.raizlabs.android.dbflow.kotlinextensions.insert
import com.raizlabs.android.dbflow.kotlinextensions.select
import org.chapper.chapper.data.bluetooth.BluetoothFactory
import org.chapper.chapper.data.bluetooth.BluetoothService
import org.chapper.chapper.data.model.Settings
import org.chapper.chapper.domain.usecase.BluetoothUseCase

class App : Application() {
    override fun onCreate() {
        super.onCreate()

        FlowManager.init(applicationContext)

        initSQLTables()

        BluetoothFactory.initBluetoothSSP(applicationContext)
        BluetoothUseCase.bluetoothStatusAction()

        startService(Intent(applicationContext, BluetoothService::class.java))
    }

    private fun initSQLTables() {
        if (!(select from Settings::class).hasData()) {
            Settings().insert()
        }
    }
}