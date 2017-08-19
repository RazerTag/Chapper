package org.chapper.chapper.presentation.screen.chatlist

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import org.chapper.chapper.R
import org.chapper.chapper.data.model.Chat

class ChatListAdapter(private val mChats: List<Chat>) : RecyclerView.Adapter<ChatHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ChatHolder {
        val itemView = LayoutInflater.from(parent!!.context)
                .inflate(R.layout.item_chat, parent, false)
        return ChatHolder(itemView)
    }

    override fun onBindViewHolder(holder: ChatHolder?, position: Int) {
        val chat = mChats[position]
        holder!!.bind(chat)
    }

    override fun getItemCount(): Int = mChats.size
}