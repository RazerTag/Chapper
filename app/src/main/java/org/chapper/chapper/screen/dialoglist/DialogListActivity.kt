package org.chapper.chapper.screen.dialoglist

import android.bluetooth.BluetoothAdapter
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import app.akexorcist.bluetotohspp.library.BluetoothState
import butterknife.bindView
import com.mikepenz.materialdrawer.Drawer
import org.chapper.chapper.R
import org.chapper.chapper.bluetooth.BluetoothFactory
import org.chapper.chapper.screen.searchdeviceslist.SearchDevicesListActivity
import org.chapper.chapper.screen.settings.SettingsActivity
import org.chapper.chapper.utils.DrawerFactory
import org.jetbrains.anko.browse
import org.jetbrains.anko.share
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast
import kotlin.properties.Delegates


class DialogListActivity : AppCompatActivity() {
    val mToolbar: Toolbar by bindView(R.id.toolbar)

    val mSearchDevicesFloatButton: FloatingActionButton by bindView(R.id.search_devices_float_button)

    var mDrawer: Drawer by Delegates.notNull()

    private val mBt = BluetoothFactory.getBluetoothSSP(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dialog_list)
        initToolbar()
        initDrawer()

        mSearchDevicesFloatButton.setOnClickListener {
            startActivity<SearchDevicesListActivity>()
        }

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
        mToolbar.navigationIcon = ContextCompat.getDrawable(this, R.drawable.menu)
        mToolbar.setNavigationOnClickListener {
            mDrawer.openDrawer()
        }
    }

    private fun initDrawer() {
        mDrawer = DrawerFactory.getDrawer(this)
        mDrawer.setOnDrawerItemClickListener { view, position, drawerItem ->
            handleDrawerItemClickListener(position)
        }
    }

    private fun initDrawer(name: String, btMacAddress: String) {
        mDrawer = DrawerFactory.getDrawer(this, name, btMacAddress)
        mDrawer.setOnDrawerItemClickListener { view, position, drawerItem ->
            handleDrawerItemClickListener(position)
        }
    }

    override fun onBackPressed() {
        if (mDrawer.isDrawerOpen) {
            mDrawer.closeDrawer()
        } else {
            super.onBackPressed()
        }
    }

    override fun onResume() {
        super.onStart()

        checkBluetoothStatus()
    }

    private fun handleDrawerItemClickListener(position: Int): Boolean {
        when (position) {
            1 -> {
                startActivity<SearchDevicesListActivity>()
            }
            3 -> {
                share(getString(R.string.sharing_text))
            }
            4 -> {
                startActivity<SettingsActivity>()
            }
            5 -> {
                browse(getString(R.string.faq_url))
            }
            else -> {
                toast(getString(R.string.error))
            }
        }
        return false
    }

    private fun checkBluetoothStatus() {
        if (!mBt.isBluetoothAvailable) {
            mToolbar.title = getString(R.string.bluetooth_not_available)
            initDrawer("Vladislav Annenkov", getString(R.string.unknown_mac_address))
        } else if (mBt.isBluetoothAvailable) {
            if (!mBt.isBluetoothEnabled) {
                mToolbar.title = getString(R.string.bluetooth_not_enabled)
            } else {
                mToolbar.title = getString(R.string.app_name)
            }

            val btMacAddress = BluetoothAdapter.getDefaultAdapter().address
            initDrawer("Vladislav Annenkov", btMacAddress)
        }
    }
}
