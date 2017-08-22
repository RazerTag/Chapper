package org.chapper.chapper.presentation.screen.chat

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import org.chapper.chapper.R
import org.chapper.chapper.data.model.Message

class ChatAdapter(private val mMessages: List<Message>) : RecyclerView.Adapter<MessageHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): MessageHolder {
        val itemView = LayoutInflater.from(parent!!.context)
                .inflate(R.layout.item_message_incoming, parent, false)
        return MessageHolder(itemView)
    }

    override fun onBindViewHolder(holder: MessageHolder?, position: Int) {
        val message = mMessages[position]
        holder!!.bind(message)
    }

    override fun getItemCount(): Int {
        TODO("not implemented")
    }
}