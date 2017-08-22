package org.chapper.chapper.presentation.screen.chat

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import org.chapper.chapper.R
import kotlin.properties.Delegates

class ChatActivity : AppCompatActivity(), ChatView {
    private var mPresenter: ChatPresenter by Delegates.notNull()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        mPresenter = ChatPresenter(this)
    }
}
