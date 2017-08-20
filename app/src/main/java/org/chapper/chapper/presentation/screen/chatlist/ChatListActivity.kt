package org.chapper.chapper.presentation.screen.chatlist

import android.bluetooth.BluetoothAdapter
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import butterknife.bindView
import com.arellomobile.mvp.MvpAppCompatActivity
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.PresenterType
import com.mikepenz.materialdrawer.Drawer
import org.chapper.chapper.R
import org.chapper.chapper.data.tables.ChatTable
import org.chapper.chapper.data.tables.SettingsTable
import org.chapper.chapper.presentation.broadcastreceivers.BluetoothStateBroadcastReceiver
import org.chapper.chapper.presentation.screen.devicelist.DeviceListActivity
import org.chapper.chapper.presentation.screen.intro.IntroActivity
import org.chapper.chapper.presentation.screen.settings.SettingsActivity
import org.chapper.chapper.presentation.utils.DrawerBuilderFactory
import org.jetbrains.anko.browse
import org.jetbrains.anko.share
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast
import ru.arturvasilov.sqlite.core.BasicTableObserver
import ru.arturvasilov.sqlite.core.SQLite
import kotlin.properties.Delegates

class ChatListActivity : MvpAppCompatActivity(), ChatListView, BasicTableObserver {
    @InjectPresenter(type = PresenterType.GLOBAL)
    lateinit var mChatListPresenter: ChatListPresenter

    private var mBtReceiverState: BluetoothStateBroadcastReceiver by Delegates.notNull()

    private val mToolbar: Toolbar by bindView(R.id.toolbar)

    private val mRecyclerView: RecyclerView by bindView(R.id.recyclerView)
    private var mAdapter: ChatListAdapter by Delegates.notNull()

    private val mSearchDevicesFloatButton: FloatingActionButton by bindView(R.id.search_devices_float_button)

    private var mDrawer: Drawer by Delegates.notNull()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_list)

        mChatListPresenter.init()
        mChatListPresenter.bluetoothStatusAction()

        SQLite.get().registerObserver(SettingsTable.TABLE, this)
        SQLite.get().registerObserver(ChatTable.TABLE, this)

        if (SettingsTable.SETTINGS.isFirstStart)
            startActivity<IntroActivity>()

        mSearchDevicesFloatButton.setOnClickListener {
            startSearchDevicesListActivity()
        }

        mBtReceiverState = BluetoothStateBroadcastReceiver(mChatListPresenter)
        registerReceiver(mBtReceiverState
                , IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED))
    }

    override fun initToolbar() {
        setSupportActionBar(mToolbar)
        mToolbar.navigationIcon = ContextCompat.getDrawable(this, R.drawable.menu)
        mToolbar.setNavigationOnClickListener {
            mDrawer.openDrawer()
        }
    }

    override fun initDrawer() {
        val drawerBuilderFactory = DrawerBuilderFactory(applicationContext,
                this.currentFocus,
                SettingsTable.SETTINGS.firstName,
                SettingsTable.SETTINGS.lastName)

        val account = drawerBuilderFactory.getHeaderBuilder()
                .withActivity(this)
                .build()

        mDrawer = drawerBuilderFactory.getDrawerBuilder()
                .withActivity(this)
                .withAccountHeader(account)
                .build()

        mDrawer.setOnDrawerItemClickListener { _, position, _ ->
            mChatListPresenter.handleDrawerItemClickListener(position)
        }
    }

    override fun initLoadingDrawer() {
        val drawerBuilderFactory = DrawerBuilderFactory(applicationContext,
                this.currentFocus,
                SettingsTable.SETTINGS.firstName,
                SettingsTable.SETTINGS.lastName)

        val account = drawerBuilderFactory.getHeaderBuilder()
                .withActivity(this)
                .build()

        mDrawer = drawerBuilderFactory.getDrawerBuilder()
                .withActivity(this)
                .withAccountHeader(account)
                .build()

        mDrawer.setOnDrawerItemClickListener { _, position, _ ->
            mChatListPresenter.handleDrawerItemClickListener(position)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_refresh, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        val id = item!!.itemId

        when (id) {
            R.id.action_refresh -> {
            } // TODO : Do something
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        if (mDrawer.isDrawerOpen) {
            mDrawer.closeDrawer()
        } else {
            super.onBackPressed()
        }
    }

    override fun onStop() {
        super.onStop()

        SQLite.get().unregisterObserver(this)
    }

    override fun onDestroy() {
        super.onDestroy()

        mRecyclerView.adapter = null
        unregisterReceiver(mBtReceiverState)
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
        val intent = Intent(applicationContext, DeviceListActivity::class.java)
        startActivity(intent)
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

    override fun btNotAvailable() {
        mToolbar.title = getString(R.string.bluetooth_not_available)
        initLoadingDrawer()
    }

    override fun btNotEnabled() {
        mToolbar.title = getString(R.string.bluetooth_not_enabled)
        initDrawer()
    }

    override fun btEnabled() {
        mToolbar.title = getString(R.string.app_name)
        initDrawer()
    }
}
