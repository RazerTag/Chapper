package org.chapper.chapper.screen.searchdeviceslist

import android.bluetooth.BluetoothAdapter
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import butterknife.bindView
import org.chapper.chapper.R
import kotlin.properties.Delegates

class SearchDevicesListActivity : AppCompatActivity() {
    val mToolbar: Toolbar by bindView(R.id.toolbar)

    private var mBt: BluetoothAdapter by Delegates.notNull()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_devices_list)
        initToolbar()

        mBt = BluetoothAdapter.getDefaultAdapter()
    }

    private fun initToolbar() {
        setSupportActionBar(mToolbar)
        mToolbar.navigationIcon = ContextCompat.getDrawable(this, R.drawable.keyboard_backspace)
        mToolbar.setNavigationOnClickListener {
            finishActivity()
        }
    }

    private fun finishActivity() {
        finish()
    }

    private fun doDiscovery() {
        // Remove all element from the list
        // mPairedDevicesArrayAdapter.clear()

        // If there are paired devices, add each one to the ArrayAdapter
        /*if (pairedDevices.size > 0) {
            for (device in pairedDevices) {
                mPairedDevicesArrayAdapter.add(device.getName() + "\n" + device.getAddress())
            }
        } else {
            var strNoFound: String? = intent.getStringExtra("no_devices_found")
            if (strNoFound == null)
                strNoFound = "No devices found"
            mPairedDevicesArrayAdapter.add(strNoFound)
        }*/

        // Indicate scanning in the title
        var strScanning: String? = intent.getStringExtra("scanning")
        if (strScanning == null)
            strScanning = "Scanning for devices..."
        setProgressBarIndeterminateVisibility(true)
        title = strScanning

        // Turn on sub-title for new devices
        // findViewById(R.id.title_new_devices).setVisibility(View.VISIBLE);
        // If we're already discovering, stop it
        if (mBt.isDiscovering) {
            mBt.cancelDiscovery()
        }

        // Request discover from BluetoothAdapter
        mBt.startDiscovery()
    }
}
