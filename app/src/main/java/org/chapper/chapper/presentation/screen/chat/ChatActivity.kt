package org.chapper.chapper.presentation.screen.chat

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import butterknife.bindView
import org.chapper.chapper.Extra
import org.chapper.chapper.R
import org.chapper.chapper.data.repository.MessageRepository
import kotlin.properties.Delegates

class ChatActivity : AppCompatActivity(), ChatView {
    private var mPresenter: ChatPresenter by Delegates.notNull()

    private val mRecyclerView: RecyclerView by bindView(R.id.recyclerView)
    private var mAdapter: ChatAdapter by Delegates.notNull()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        mPresenter = ChatPresenter(this)

        mPresenter.init()
    }

    override fun showMessages() {
        mRecyclerView.setHasFixedSize(false)
        mRecyclerView.layoutManager = LinearLayoutManager(this)
        mAdapter = ChatAdapter(MessageRepository
                .getMessages(intent
                        .getStringExtra(Extra.CHAT_ID_EXTRA)))
        mRecyclerView.adapter = mAdapter
    }
}
