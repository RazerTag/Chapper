package org.chapper.chapper.presentation.screen.chat

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import butterknife.bindView
import org.chapper.chapper.R
import org.chapper.chapper.data.Constants
import org.chapper.chapper.data.repository.ChatRepository
import org.chapper.chapper.data.repository.MessageRepository
import org.chapper.chapper.domain.usecase.BluetoothUseCase
import org.chapper.chapper.domain.usecase.NotificationUseCase
import org.jetbrains.anko.toast
import tk.wasdennnoch.progresstoolbar.ProgressToolbar
import kotlin.properties.Delegates


class ChatActivity : AppCompatActivity(), ChatView {
    private var mPresenter: ChatPresenter by Delegates.notNull()

    private val mToolbar: Toolbar by bindView(R.id.toolbar)
    private val mChatName: TextView by bindView(R.id.chatName)
    private val mChatStatus: TextView by bindView(R.id.chatStatus)
    private val mChatPhoto: TextView by bindView(R.id.profile_image)

    private val mRecyclerView: RecyclerView by bindView(R.id.recyclerView)
    private var mAdapter: ChatAdapter by Delegates.notNull()

    private val mSendButton: ImageButton by bindView(R.id.sendButton)
    private val mMessageEditText: EditText by bindView(R.id.messageEditText)

    private val mRefresher: ProgressToolbar by bindView(R.id.refresher)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)
        window.setBackgroundDrawableResource(R.drawable.background)
        mPresenter = ChatPresenter(this)

        mPresenter.init(applicationContext, intent)

        mSendButton.setOnClickListener {
            sendMessage()
        }

        mMessageEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {}

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (p0 != null && p0.isNotEmpty())
                    BluetoothUseCase.send(Constants.TYPING)
            }
        })
    }

    override fun onResume() {
        super.onResume()

        NotificationUseCase.cleatAll(applicationContext)
    }

    override fun initToolbar() {
        setSupportActionBar(mToolbar)
        mChatName.text = ChatRepository.getName(mPresenter.mChat)

        mChatPhoto.text = ChatRepository.getFirstCharsName(mPresenter.mChat)

        /*val photo = ImageRepository.getImage(applicationContext, mPresenter.mChatId)
        if (photo != null) TODO : Do set custom photo
            mChatPhoto.setImageBitmap(photo)*/

        mToolbar.navigationIcon = ContextCompat.getDrawable(this, R.drawable.arrow_left_white)
        mToolbar.setNavigationOnClickListener {
            finish()
        }
    }

    override fun showMessages() {
        mRecyclerView.setHasFixedSize(false)
        val layout = LinearLayoutManager(this)
        layout.stackFromEnd = true
        mRecyclerView.layoutManager = layout
        mAdapter = ChatAdapter(MessageRepository
                .getMessages(intent
                        .getStringExtra(Constants.CHAT_ID_EXTRA)))
        mRecyclerView.adapter = mAdapter
    }

    override fun changeMessageList() {
        runOnUiThread {
            val messages = MessageRepository.getMessages(mPresenter.mChatId)
            mAdapter.changeDataSet(messages)
            mRecyclerView.smoothScrollToPosition(messages.size - 1)
        }
    }

    override fun sendMessage() {
        if (isCoarseLocationPermissionDenied()) {
            requestCoarseLocationPermission()
            return
        }

        mPresenter.sendMessage(mMessageEditText.text.toString())
        mMessageEditText.setText("")
    }

    override fun onDestroy() {
        super.onDestroy()

        mRecyclerView.adapter = null
        mPresenter.destroy(applicationContext)
    }

    override fun showRefresher() {
        mRefresher.visibility = View.VISIBLE
    }

    override fun hideRefresher() {
        mRefresher.visibility = View.INVISIBLE
    }

    override fun startRefreshing() {
        mPresenter.startDiscovery()
    }

    override fun statusTyping() {
        mChatStatus.text = getString(R.string.typing)
    }

    override fun statusConnected() {
        mChatStatus.text = getString(R.string.connected)
    }

    override fun statusNearby() {
        mChatStatus.text = getString(R.string.nearby)
    }

    override fun statusOffline() {
        mChatStatus.text = mPresenter.mChat.getLastConnectionString(applicationContext)
    }

    override fun isCoarseLocationPermissionDenied(): Boolean {
        val status = ActivityCompat.checkSelfPermission(applicationContext, Manifest.permission.ACCESS_COARSE_LOCATION)
        return status == PackageManager.PERMISSION_DENIED
    }

    override fun requestCoarseLocationPermission() {
        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION),
                Constants.COARSE_LOCATION_PERMISSIONS)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            Constants.COARSE_LOCATION_PERMISSIONS -> {
                if (grantResults.size == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    sendMessage()
                } else {
                    toast(resources.getString(R.string.error))
                }
            }
        }
    }
}
