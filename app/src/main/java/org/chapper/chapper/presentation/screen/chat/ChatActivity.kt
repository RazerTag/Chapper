package org.chapper.chapper.presentation.screen.chat

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.EditText
import android.widget.ImageButton
import butterknife.bindView
import com.raizlabs.android.dbflow.runtime.FlowContentObserver
import org.chapper.chapper.R
import org.chapper.chapper.data.Extra
import org.chapper.chapper.data.model.AppAction
import org.chapper.chapper.data.model.Chat
import org.chapper.chapper.data.model.Message
import org.chapper.chapper.data.model.Settings
import org.chapper.chapper.data.repository.MessageRepository
import kotlin.properties.Delegates

class ChatActivity : AppCompatActivity(), ChatView {
    private var mPresenter: ChatPresenter by Delegates.notNull()

    private var chatId: String by Delegates.notNull()

    private val mRecyclerView: RecyclerView by bindView(R.id.recyclerView)
    private var mAdapter: ChatAdapter by Delegates.notNull()

    private val mSendButton: ImageButton by bindView(R.id.sendButton)
    private val mMessageEditText: EditText by bindView(R.id.messageEditText)

    private var mFlowObserver: FlowContentObserver by Delegates.notNull()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)
        mPresenter = ChatPresenter(this)
        mPresenter.init()

        chatId = intent.getStringExtra(Extra.CHAT_ID_EXTRA)

        mFlowObserver = FlowContentObserver()
        mFlowObserver.registerForContentChanges(applicationContext, Settings::class.java)
        mFlowObserver.registerForContentChanges(applicationContext, Chat::class.java)
        mFlowObserver.registerForContentChanges(applicationContext, Message::class.java)
        mFlowObserver.registerForContentChanges(applicationContext, AppAction::class.java)
        mPresenter.databaseChangesListener(mFlowObserver)

        mSendButton.setOnClickListener {
            mPresenter.sendMessage(chatId, mMessageEditText.text.toString())
            mMessageEditText.setText("")
        }
    }

    override fun showMessages() {
        mRecyclerView.setHasFixedSize(false)
        val layout = LinearLayoutManager(this)
        layout.stackFromEnd = true
        mRecyclerView.layoutManager = layout
        mAdapter = ChatAdapter(MessageRepository
                .getMessages(intent
                        .getStringExtra(Extra.CHAT_ID_EXTRA)))
        mRecyclerView.adapter = mAdapter
    }

    override fun changeMessageList() {
        runOnUiThread {
            val messages = MessageRepository.getMessages(chatId)
            mAdapter.changeDataSet(messages)
            mRecyclerView.smoothScrollToPosition(messages.size - 1)
        }
    }
}
