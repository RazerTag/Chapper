package org.chapper.chapper.screen.chatlist

import android.bluetooth.BluetoothAdapter
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import app.akexorcist.bluetotohspp.library.BluetoothState
import app.akexorcist.bluetotohspp.library.DeviceList
import butterknife.bindView
import com.mikepenz.materialdrawer.Drawer
import org.chapper.chapper.R
import org.chapper.chapper.bluetooth.BluetoothFactory
import org.chapper.chapper.data.model.Chat
import org.chapper.chapper.data.model.Settings
import org.chapper.chapper.data.tables.SettingsTable
import org.chapper.chapper.screen.intro.IntroActivity
import org.chapper.chapper.screen.searchdeviceslist.SearchDevicesListActivity
import org.chapper.chapper.screen.settings.SettingsActivity
import org.chapper.chapper.utils.DrawerFactory
import org.jetbrains.anko.browse
import org.jetbrains.anko.share
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast
import ru.arturvasilov.sqlite.core.BasicTableObserver
import ru.arturvasilov.sqlite.core.SQLite
import kotlin.properties.Delegates


class ChatListActivity : AppCompatActivity(), BasicTableObserver {
    val mToolbar: Toolbar by bindView(R.id.toolbar)

    val mRecyclerView: RecyclerView by bindView(R.id.recyclerView)
    var mAdapter: ChatsAdapter by Delegates.notNull()

    val mSearchDevicesFloatButton: FloatingActionButton by bindView(R.id.search_devices_float_button)

    var mDrawer: Drawer by Delegates.notNull()

    private val mBt = BluetoothFactory.getBluetoothSSP(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_list)

        initSQLTables()
        initToolbar()
        initDrawer()
        showDialogs()

        SQLite.get().registerObserver(SettingsTable.TABLE, this)

        if (SettingsTable.SETTINGS.isFirstStart) {
            startActivity<IntroActivity>()
        }

        mSearchDevicesFloatButton.setOnClickListener {
            //startActivity<SearchDevicesListActivity>()
            val intent = Intent(applicationContext, DeviceList::class.java)
            startActivityForResult(intent, BluetoothState.REQUEST_CONNECT_DEVICE)
        }

        mBt.setBluetoothStateListener { state ->
            when (state) {
                BluetoothState.REQUEST_ENABLE_BT -> {
                    checkBluetoothStatus()
                }
                BluetoothState.STATE_NONE -> {
                    mToolbar.title = getString(R.string.app_name)
                }
            }
        }

        registerReceiver(object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                checkBluetoothStatus()
            }
        }, IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED))
    }

    private fun initToolbar() {
        setSupportActionBar(mToolbar)
        mToolbar.navigationIcon = ContextCompat.getDrawable(this, R.drawable.menu)
        mToolbar.setNavigationOnClickListener {
            mDrawer.openDrawer()
        }
    }

    private fun initDrawer() {
        val btMacAddress = BluetoothAdapter.getDefaultAdapter().address
        mDrawer = DrawerFactory.getDrawer(this, SettingsTable.SETTINGS.firstName, SettingsTable.SETTINGS.lastName, btMacAddress)
        mDrawer.setOnDrawerItemClickListener { _, position, _ ->
            handleDrawerItemClickListener(position)
        }
    }

    private fun initDrawer(btMacAddress: String) {
        mDrawer = DrawerFactory.getDrawer(this, SettingsTable.SETTINGS.firstName, SettingsTable.SETTINGS.lastName, btMacAddress)
        mDrawer.setOnDrawerItemClickListener { _, position, _ ->
            handleDrawerItemClickListener(position)
        }
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

    override fun onTableChanged() {
        initDrawer()
        showDialogs()
    }

    private fun showDialogs() {
        mRecyclerView.setHasFixedSize(false)
        mRecyclerView.layoutManager = LinearLayoutManager(this)
        mAdapter = ChatsAdapter(getChats())
        mRecyclerView.adapter = mAdapter
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
            initDrawer(getString(R.string.unknown_mac_address))
        } else if (mBt.isBluetoothAvailable) {
            if (!mBt.isBluetoothEnabled) {
                mToolbar.title = getString(R.string.bluetooth_not_enabled)
            } else {
                mToolbar.title = getString(R.string.app_name)
            }

            initDrawer()
        }
    }

    private fun getChats(): ArrayList<Chat> {
        val chats = arrayListOf<Chat>()

        // TODO: Getting chats

        return chats
    }
}
