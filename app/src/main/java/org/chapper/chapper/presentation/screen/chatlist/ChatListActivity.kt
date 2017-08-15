package org.chapper.chapper.presentation.screen.chatlist

import android.bluetooth.BluetoothAdapter
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import app.akexorcist.bluetotohspp.library.BluetoothState
import app.akexorcist.bluetotohspp.library.DeviceList
import butterknife.bindView
import com.arellomobile.mvp.MvpAppCompatActivity
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.PresenterType
import com.mikepenz.materialdrawer.Drawer
import org.chapper.chapper.R
import org.chapper.chapper.data.bluetooth.BluetoothFactory
import org.chapper.chapper.data.model.Settings
import org.chapper.chapper.data.tables.ChatTable
import org.chapper.chapper.data.tables.SettingsTable
import org.chapper.chapper.presentation.screen.intro.IntroActivity
import org.chapper.chapper.presentation.screen.searchdeviceslist.SearchDevicesListActivity
import org.chapper.chapper.presentation.screen.settings.SettingsActivity
import org.chapper.chapper.presentation.utils.DrawerFactory
import org.jetbrains.anko.browse
import org.jetbrains.anko.share
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast
import ru.arturvasilov.sqlite.core.BasicTableObserver
import ru.arturvasilov.sqlite.core.SQLite
import kotlin.properties.Delegates


class ChatListActivity : MvpAppCompatActivity(), BasicTableObserver, ChatListView {
    @InjectPresenter(type = PresenterType.GLOBAL)
    lateinit var mChatListPresenter: ChatListPresenter

    private val mToolbar: Toolbar by bindView(R.id.toolbar)

    private val mRecyclerView: RecyclerView by bindView(R.id.recyclerView)

    private var mAdapter: ChatListAdapter by Delegates.notNull()
    private val mSearchDevicesFloatButton: FloatingActionButton by bindView(R.id.search_devices_float_button)

    private var mDrawer: Drawer by Delegates.notNull()

    private val mBt = BluetoothFactory.getBluetoothSSP(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_list)

        mChatListPresenter.init()

        SQLite.get().registerObserver(SettingsTable.TABLE, this)
        SQLite.get().registerObserver(ChatTable.TABLE, this)

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
    }

    override fun initToolbar() {
        setSupportActionBar(mToolbar)
        mToolbar.navigationIcon = ContextCompat.getDrawable(this, R.drawable.menu)
        mToolbar.setNavigationOnClickListener {
            mDrawer.openDrawer()
        }
    }

    override fun initDrawer() {
        val btMacAddress = BluetoothAdapter.getDefaultAdapter().address
        initDrawer(btMacAddress)
    }

    private fun initDrawer(btMacAddress: String) {
        mDrawer = DrawerFactory.getDrawer(this, SettingsTable.SETTINGS.firstName, SettingsTable.SETTINGS.lastName, btMacAddress)
        mDrawer.setOnDrawerItemClickListener { _, position, _ ->
            mChatListPresenter.handleDrawerItemClickListener(position)
        }
    }

    override fun initSQLTables() {
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

    override fun onStop() {
        super.onStop()
        SQLite.get().unregisterObserver(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        mRecyclerView.adapter = null
    }

    override fun onTableChanged() {
        initDrawer()
        showDialogs()
    }

    override fun showDialogs() {
        mRecyclerView.setHasFixedSize(false)
        mRecyclerView.layoutManager = LinearLayoutManager(this)
        mAdapter = ChatListAdapter(ChatTable.chats)
        mRecyclerView.adapter = mAdapter
    }

    override fun startSearchDevicesListActivity() {
        startActivity<SearchDevicesListActivity>()
    }

    override fun shareWithFriends() {
        share(getString(R.string.sharing_text))
    }

    override fun startSettingsActivity() {
        startActivity<SettingsActivity>()
    }

    override fun openFaqInBrowser() {
        browse(getString(R.string.faq_url))
    }

    override fun showError() {
        toast(getString(R.string.error))
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
}