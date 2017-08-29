package org.chapper.chapper.presentation.app

import android.app.Application
import android.bluetooth.BluetoothAdapter
import android.content.IntentFilter
import com.raizlabs.android.dbflow.config.FlowManager
import com.raizlabs.android.dbflow.kotlinextensions.from
import com.raizlabs.android.dbflow.kotlinextensions.insert
import com.raizlabs.android.dbflow.kotlinextensions.select
import org.chapper.chapper.data.bluetooth.BluetoothFactory
import org.chapper.chapper.data.model.Settings
import org.chapper.chapper.presentation.broadcastreceiver.BluetoothStateBroadcastReceiver
import kotlin.properties.Delegates

class App : Application(), AppView {
    private var mPresenter: AppPresenter by Delegates.notNull()

    private var mBtReceiverState: BluetoothStateBroadcastReceiver by Delegates.notNull()

    override fun onCreate() {
        super.onCreate()
        mPresenter = AppPresenter(this)

        FlowManager.init(this)

        initSQLTables()

        BluetoothFactory.initBluetoothSSP(applicationContext)
        mPresenter.bluetoothStatusAction()

        mPresenter.registerBluetoothStateReceiver()
        mPresenter.bluetoothConnectionListener(applicationContext)
        mPresenter.onDataReceivedListener()
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

    override fun registerReceiver(listener: BluetoothStateBroadcastReceiver.ActionListener) {
        mBtReceiverState = BluetoothStateBroadcastReceiver(listener)

        registerReceiver(mBtReceiverState
                , IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED))
    }
}