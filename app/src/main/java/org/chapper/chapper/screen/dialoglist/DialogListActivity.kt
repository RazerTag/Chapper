package org.chapper.chapper.screen.dialoglist

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import app.akexorcist.bluetotohspp.library.BluetoothState
import butterknife.bindView
import com.mikepenz.materialdrawer.Drawer
import org.chapper.chapper.R
import org.chapper.chapper.bluetooth.BluetoothFactory
import org.chapper.chapper.utils.DrawerFactory

class DialogListActivity : AppCompatActivity() {
    val mToolbar: Toolbar by bindView(R.id.toolbar)

    private val mBt = BluetoothFactory.getBluetoothSSP(this)

    private var mDrawer: Drawer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dialog_list)
        initToolbar()

        mDrawer = DrawerFactory.getDrawer(this)

        mBt.setBluetoothStateListener { state ->
            when (state) {
                BluetoothState.REQUEST_ENABLE_BT -> {
                    // later
                }
                BluetoothState.STATE_NONE -> {
                    mToolbar.title = getString(R.string.app_name)
                }
            }

            checkBluetoothStatus()
        }
    }

    private fun initToolbar() {
        setSupportActionBar(mToolbar)
    }

    override fun onBackPressed() {
        if (mDrawer!!.isDrawerOpen) {
            mDrawer!!.closeDrawer()
        } else {
            super.onBackPressed()
        }
    }

    override fun onResume() {
        super.onStart()

        checkBluetoothStatus()
    }

    private fun checkBluetoothStatus() {
        if (!mBt.isBluetoothAvailable) {
            mToolbar.title = getString(R.string.bluetooth_not_available)
        } else if (mBt.isBluetoothAvailable) {
            if (!mBt.isBluetoothEnabled) {
                mToolbar.title = getString(R.string.bluetooth_not_enabled)
            } else {
                mToolbar.title = getString(R.string.app_name)
            }
        }
    }
}
