package org.chapper.chapper.presentation.screen.devicelist

import android.Manifest
import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
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
import android.view.View
import app.akexorcist.bluetotohspp.library.BluetoothState
import butterknife.bindView
import org.chapper.chapper.R
import org.chapper.chapper.data.model.Device
import org.chapper.chapper.presentation.broadcastreceiver.BluetoothDiscoveryBroadcastReceiver
import org.jetbrains.anko.toast
import kotlin.properties.Delegates

class DeviceListActivity : AppCompatActivity(), DeviceListView {
    private var mPresenter: DeviceListPresenter by Delegates.notNull()

    private val REQUEST_CODE_COARSE_LOCATION_PERMISSIONS = 1

    private val mToolbar: Toolbar by bindView(R.id.toolbar)
    private val mNoOneNearBlock: View by bindView(R.id.noOneNear)

    private var mReceiver: BroadcastReceiver by Delegates.notNull()
    private var mBtAdapter: BluetoothAdapter? = null

    private val mRecyclerView: RecyclerView by bindView(R.id.recyclerView)
    private var mPairedDeviceArrayAdapter: DeviceListAdapter by Delegates.notNull()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_device_list)
        mPresenter = DeviceListPresenter(this)

        mPresenter.init()

        setResult(Activity.RESULT_CANCELED, Intent())

        mPresenter.registerReceiver()

        mBtAdapter = BluetoothAdapter.getDefaultAdapter()

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
                onDeviceSelect(device.bluetoothName, device.bluetoothAddress)
            }
        })
        mRecyclerView.adapter = mPairedDeviceArrayAdapter
    }

    override fun addDevice(device: Device) {
        mPairedDeviceArrayAdapter.addDevice(device)
    }

    override fun showNoOneNearBlock() {
        mNoOneNearBlock.visibility = View.VISIBLE
    }

    override fun hideNoOneNearBlock() {
        mNoOneNearBlock.visibility = View.INVISIBLE
    }

    override fun setRefreshingTitle() {
        mToolbar.title = getString(R.string.refreshing)
    }

    override fun setSectionNameTitle() {
        mToolbar.title = getString(R.string.search_for_devices)
    }

    override fun registerReceiver(listener: BluetoothDiscoveryBroadcastReceiver.ActionListener) {
        mReceiver = BluetoothDiscoveryBroadcastReceiver(listener)

        var filter = IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_STARTED)
        this.registerReceiver(mReceiver, filter)

        filter = IntentFilter(BluetoothDevice.ACTION_FOUND)
        this.registerReceiver(mReceiver, filter)

        filter = IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)
        this.registerReceiver(mReceiver, filter)
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
        if (mBtAdapter != null) {
            mBtAdapter!!.cancelDiscovery()
        }

        this.unregisterReceiver(mReceiver)
    }

    private fun doDiscovery() {
        val hasPermission = ActivityCompat.checkSelfPermission(this@DeviceListActivity, Manifest.permission.ACCESS_COARSE_LOCATION)
        if (hasPermission == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this@DeviceListActivity, arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION),
                    REQUEST_CODE_COARSE_LOCATION_PERMISSIONS)
            return
        }

        mToolbar.title = getString(R.string.refreshing)

        mPairedDeviceArrayAdapter.clear()

        if (mBtAdapter!!.isDiscovering) {
            mBtAdapter!!.cancelDiscovery()
        }

        mBtAdapter!!.startDiscovery()
    }

    fun onDeviceSelect(name: String, address: String) {
        if (mBtAdapter!!.isDiscovering)
            mBtAdapter!!.cancelDiscovery()

        val intent = Intent()
        intent.putExtra(BluetoothState.DEVICE_NAME, name)
        intent.putExtra(BluetoothState.EXTRA_DEVICE_ADDRESS, address)

        setResult(Activity.RESULT_OK, intent)
        finish()
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
