package org.chapper.chapper.presentation.screen.chat

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import butterknife.bindView
import org.chapper.chapper.R
import org.chapper.chapper.data.model.Message

class ActionMessageHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val mMessageText: TextView by bindView(R.id.message_text)

    fun bind(message: Message) {
        mMessageText.text = message.text
    }
}