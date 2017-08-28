package org.chapper.chapper.presentation.screen.chatlist

import android.bluetooth.BluetoothAdapter
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import app.akexorcist.bluetotohspp.library.BluetoothState
import butterknife.bindView
import com.mikepenz.materialdrawer.Drawer
import com.raizlabs.android.dbflow.runtime.FlowContentObserver
import org.chapper.chapper.R
import org.chapper.chapper.data.Extra
import org.chapper.chapper.data.model.AppAction
import org.chapper.chapper.data.model.Chat
import org.chapper.chapper.data.model.Message
import org.chapper.chapper.data.model.Settings
import org.chapper.chapper.data.repository.ChatRepository
import org.chapper.chapper.data.repository.SettingsRepository
import org.chapper.chapper.presentation.screen.chat.ChatActivity
import org.chapper.chapper.presentation.screen.devicelist.DeviceListActivity
import org.chapper.chapper.presentation.screen.intro.IntroActivity
import org.chapper.chapper.presentation.screen.settings.SettingsActivity
import org.chapper.chapper.presentation.util.DrawerBuilderFactory
import org.jetbrains.anko.browse
import org.jetbrains.anko.share
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast
import kotlin.properties.Delegates

class ChatListActivity : AppCompatActivity(), ChatListView {
    private var mPresenter: ChatListPresenter by Delegates.notNull()

    private val mToolbar: Toolbar by bindView(R.id.toolbar)
    private var mDrawer: Drawer by Delegates.notNull()

    private val mRecyclerView: RecyclerView by bindView(R.id.recyclerView)
    private var mAdapter: ChatListAdapter by Delegates.notNull()

    private val mSearchDevicesFloatButton: FloatingActionButton by bindView(R.id.search_devices_float_button)

    private var mFlowObserver: FlowContentObserver by Delegates.notNull()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_list)
        mPresenter = ChatListPresenter(this)

        mPresenter.init()
        mPresenter.bluetoothStatusAction()

        mFlowObserver = FlowContentObserver()
        mFlowObserver.registerForContentChanges(applicationContext, Settings::class.java)
        mFlowObserver.registerForContentChanges(applicationContext, Chat::class.java)
        mFlowObserver.registerForContentChanges(applicationContext, Message::class.java)
        mFlowObserver.registerForContentChanges(applicationContext, AppAction::class.java)
        mPresenter.databaseChangesListener(mFlowObserver)

        if (SettingsRepository.isFirstStart())
            startActivity<IntroActivity>()

        mSearchDevicesFloatButton.setOnClickListener {
            startSearchDevicesListActivity()
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
        runOnUiThread {
            val drawerBuilderFactory = DrawerBuilderFactory(applicationContext,
                    this.currentFocus,
                    SettingsRepository.getFirstName(),
                    SettingsRepository.getLastName())

            val account = drawerBuilderFactory.getHeaderBuilder()
                    .withActivity(this)
                    .build()

            mDrawer = drawerBuilderFactory.getDrawerBuilder()
                    .withActivity(this)
                    .withAccountHeader(account)
                    .build()

            mDrawer.setOnDrawerItemClickListener { _, position, _ ->
                mPresenter.handleDrawerItemClickListener(position)
            }
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

    override fun onDestroy() {
        super.onDestroy()

        mRecyclerView.adapter = null
        mFlowObserver.unregisterForContentChanges(applicationContext)
    }

    override fun showChats() {
        mRecyclerView.setHasFixedSize(false)
        mRecyclerView.layoutManager = LinearLayoutManager(this)
        mAdapter = ChatListAdapter(ChatRepository.getChatsSorted(), object : ChatListAdapter.OnItemClickListener {
            override fun onItemClick(chat: Chat) {
                val intent = Intent(applicationContext, ChatActivity::class.java)
                intent.putExtra(Extra.CHAT_ID_EXTRA, chat.id)
                startActivity(intent)
            }
        })
        mRecyclerView.adapter = mAdapter
    }

    override fun changeChatList() {
        runOnUiThread {
            mAdapter.changeDataSet(ChatRepository.getChatsSorted())
        }
    }

    override fun startSearchDevicesListActivity() {
        val intent = Intent(applicationContext, DeviceListActivity::class.java)
        startActivityForResult(intent, BluetoothState.REQUEST_CONNECT_DEVICE)
    }

    override fun startEnableBluetoothDiscoverableActivity() {
        val intent = Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE)
        intent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300)
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
        initDrawer()
    }

    override fun btNotEnabled() {
        mToolbar.title = getString(R.string.bluetooth_not_enabled)
        initDrawer()
    }

    override fun btEnabled() {
        mToolbar.title = getString(R.string.app_name)
        initDrawer()
    }

    override fun showToast(text: String) {
        toast(text)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        mPresenter.activityResult(applicationContext, requestCode, resultCode, data)
    }
}
