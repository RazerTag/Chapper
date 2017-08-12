package org.chapper.chapper.presentation.screen.chatlist

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import butterknife.bindView
import org.chapper.chapper.R
import org.chapper.chapper.data.model.Chat

class ChatHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {
    val mChatName: TextView by bindView(R.id.chat_name)
    val mChatPreview: TextView by bindView(R.id.chat_preview)
    val mNewMessagesCounter: TextView by bindView(R.id.new_messages_counter)
    val mLastMessageTime: TextView by bindView(R.id.last_message_time)

    fun bind(chat: Chat) {
        mChatName.text = "${chat.firstName} ${chat.lastName}"
        mChatPreview.text = "Some text is beautiful. Right? Riiiiiiiiiiight!"
        mNewMessagesCounter.text = "3"
        mLastMessageTime.text = "13:08"
    }
}