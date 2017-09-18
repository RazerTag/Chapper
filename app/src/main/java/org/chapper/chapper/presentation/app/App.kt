package org.chapper.chapper.presentation.app

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
import kotlin.properties.Delegates

class App : Application(), AppView {
    private var mPresenter: AppPresenter by Delegates.notNull()

    override fun onCreate() {
        super.onCreate()
        mPresenter = AppPresenter(this)

        FlowManager.init(this)

        initSQLTables()

        BluetoothFactory.initBluetoothSSP(applicationContext)
        BluetoothUseCase.bluetoothStatusAction(applicationContext)

        startService(Intent(applicationContext, BluetoothService::class.java))
    }

    private fun initSQLTables() {
        if (!(select from Settings::class).hasData()) {
            Settings().insert()
        }
    }
}