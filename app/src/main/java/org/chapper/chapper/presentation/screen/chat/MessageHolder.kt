package org.chapper.chapper.presentation.screen.chat

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import kotterknife.bindView
import org.chapper.chapper.R
import org.chapper.chapper.data.model.Message
import org.chapper.chapper.data.status.MessageStatus
import org.jetbrains.anko.image

class MessageHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val mMessageText: TextView by bindView(R.id.message_text)
    private val mMessageTime: TextView by bindView(R.id.message_time)
    private val mMessageStatus: ImageView by bindView(R.id.message_status)

    fun bind(message: Message) {
        mMessageText.text = message.text
        if (!message.isAction())
            mMessageTime.text = message.getTimeString()

        when (message.status) {
            MessageStatus.OUTGOING_READ ->
                mMessageStatus.image = itemView.resources.getDrawable(R.drawable.check_all)

            MessageStatus.OUTGOING_UNREAD ->
                mMessageStatus.image = itemView.resources.getDrawable(R.drawable.check)

            MessageStatus.OUTGOING_NOT_SENT ->
                mMessageStatus.image = itemView.resources.getDrawable(R.drawable.clock)

            else -> {
                // Nothing
            }
        }
    }
}