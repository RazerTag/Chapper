package org.chapper.chapper.presentation.screen.devicelist

import android.Manifest
import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import app.akexorcist.bluetotohspp.library.BluetoothState
import butterknife.bindView
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.PresenterType
import org.chapper.chapper.R
import org.chapper.chapper.data.model.Device
import org.jetbrains.anko.toast
import kotlin.properties.Delegates

class DeviceListActivity : AppCompatActivity(), DeviceListView {
    @InjectPresenter(type = PresenterType.GLOBAL)
    lateinit var mDeviceListPresenter: DeviceListPresenter

    private val REQUEST_CODE_COARSE_LOCATION_PERMISSIONS = 1

    private val mToolbar: Toolbar by bindView(R.id.toolbar)

    // Member fields
    private var mBtAdapter: BluetoothAdapter? = null
    private val mRecyclerView: RecyclerView by bindView(R.id.recyclerView)
    private var mPairedDeviceArrayAdapter: DeviceListAdapter by Delegates.notNull()
    private var mPairedDevices: Set<BluetoothDevice> by Delegates.notNull()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_device_list)

        initToolbar()
        initDevices()

        // Set result CANCELED in case the user backs out
        setResult(Activity.RESULT_CANCELED)

        // Register for broadcasts when a device is discovered
        var filter = IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_STARTED)
        this.registerReceiver(mReceiver, filter)

        // Register for broadcasts when a device is discovered
        filter = IntentFilter(BluetoothDevice.ACTION_FOUND)
        this.registerReceiver(mReceiver, filter)

        // Register for broadcasts when discovery has finished
        filter = IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)
        this.registerReceiver(mReceiver, filter)

        // Get the local Bluetooth adapter
        mBtAdapter = BluetoothAdapter.getDefaultAdapter()

        // Get a set of currently paired devices
        mPairedDevices = mBtAdapter!!.bondedDevices

        doDiscovery()
    }

    override fun initToolbar() {
        setSupportActionBar(mToolbar)
        mToolbar.navigationIcon = ContextCompat.getDrawable(this, R.drawable.arrow_left_white)
        mToolbar.setNavigationOnClickListener {
            finish()
        }
    }

    override fun initDevices() {
        mRecyclerView.setHasFixedSize(false)
        mRecyclerView.layoutManager = LinearLayoutManager(this)
        mPairedDeviceArrayAdapter = DeviceListAdapter(arrayListOf(), object : DeviceListAdapter.OnItemClickListener {
            override fun onItemClick(device: Device) {
                onDeviceSelect(device.bluetoothAddress)
            }
        })
        mRecyclerView.adapter = mPairedDeviceArrayAdapter
    }

    override fun addDevice(device: Device) {
        mPairedDeviceArrayAdapter.addDevice(device)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_refresh, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        val id = item!!.itemId

        when (id) {
            R.id.action_refresh -> doDiscovery()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroy() {
        super.onDestroy()
        // Make sure we're not doing discovery anymore
        if (mBtAdapter != null) {
            mBtAdapter!!.cancelDiscovery()
        }

        this.unregisterReceiver(mReceiver)
        this.finish()
    }

    // Start device discover with the BluetoothAdapter
    private fun doDiscovery() {
        val hasPermission = ActivityCompat.checkSelfPermission(this@DeviceListActivity, Manifest.permission.ACCESS_COARSE_LOCATION)
        if (hasPermission == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this@DeviceListActivity, arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION),
                    REQUEST_CODE_COARSE_LOCATION_PERMISSIONS)
            return
        }

        mToolbar.title = getString(R.string.refreshing)

        // Remove all element from the list
        mPairedDeviceArrayAdapter.clear()

        // Turn on sub-title for new devices
        // findViewById(R.id.title_new_devices).setVisibility(View.VISIBLE);
        // If we're already discovering, stop it
        if (mBtAdapter!!.isDiscovering) {
            mBtAdapter!!.cancelDiscovery()
        }

        // Request discover from BluetoothAdapter
        mBtAdapter!!.startDiscovery()
    }

    fun onDeviceSelect(address: String) {
        // Cancel discovery because it's costly and we're about to connect
        if (mBtAdapter!!.isDiscovering)
            mBtAdapter!!.cancelDiscovery()

        // Create the result Intent and include the MAC address
        val intent = Intent()
        intent.putExtra(BluetoothState.EXTRA_DEVICE_ADDRESS, address)

        // Set result and finish this Activity
        setResult(Activity.RESULT_OK, intent)
        finish()
    }

    // The BroadcastReceiver that listens for discovered devices and
    // changes the title when discovery is finished
    private val mReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val action = intent.action

            // When discovery finds a device
            if (BluetoothDevice.ACTION_FOUND == action) {
                // Get the BluetoothDevice object from the Intent
                val device = intent.getParcelableExtra<BluetoothDevice>(BluetoothDevice.EXTRA_DEVICE)

                // If it's already paired, skip it, because it's been listed already
                if (device.bondState != BluetoothDevice.BOND_BONDED) {
                    var strNoFound: String? = getIntent().getStringExtra("no_devices_found")
                    if (strNoFound == null)
                        strNoFound = "No devices found"

                    val deviceModel = Device()
                    if (device.name == null || device.name == "") return
                    else deviceModel.bluetoothName = device.name
                    deviceModel.bluetoothAddress = device.address
                    addDevice(deviceModel)
                }

                // When discovery is finished, change the Activity title
            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED == action) {
                mToolbar.title = getString(R.string.search_for_devices)
            } else if (BluetoothAdapter.ACTION_DISCOVERY_STARTED == action) {
                mToolbar.title = getString(R.string.refreshing)
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            REQUEST_CODE_COARSE_LOCATION_PERMISSIONS -> {
                if (grantResults.size == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    doDiscovery()
                } else {
                    toast(resources.getString(R.string.error))
                }
            }
        }
    }
}
