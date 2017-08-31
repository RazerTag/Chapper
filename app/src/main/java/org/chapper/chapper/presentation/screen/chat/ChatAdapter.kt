package org.chapper.chapper.presentation.screen.chat

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import org.chapper.chapper.R
import org.chapper.chapper.data.MessageStatus
import org.chapper.chapper.data.model.Message
import kotlin.properties.Delegates

class ChatAdapter(private val mMessages: MutableList<Message>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val ACTION = 1
    private val INCOMING_TYPE = 2
    private val INCOMING_FIRST_TYPE = 3
    private val OUTGOING_TYPE = 4
    private val OUTGOING_FIRST_TYPE = 5

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder {
        var itemView: View by Delegates.notNull()

        when (viewType) {
            INCOMING_TYPE -> {
                itemView = LayoutInflater.from(parent!!.context)
                        .inflate(R.layout.item_message_incoming, parent, false)
            }
            INCOMING_FIRST_TYPE -> {
                itemView = LayoutInflater.from(parent!!.context)
                        .inflate(R.layout.item_message_incoming_with_angle, parent, false)
            }
            OUTGOING_TYPE -> {
                itemView = LayoutInflater.from(parent!!.context)
                        .inflate(R.layout.item_message_outgoing, parent, false)
            }
            OUTGOING_FIRST_TYPE -> {
                itemView = LayoutInflater.from(parent!!.context)
                        .inflate(R.layout.item_message_outgoing_with_angle, parent, false)
            }
            else -> {
                itemView = LayoutInflater.from(parent!!.context)
                        .inflate(R.layout.item_message_action, parent, false)
            }
        }

        return MessageHolder(itemView)
    }

    override fun onBindViewHolder(holderAction: RecyclerView.ViewHolder?, position: Int) {
        val message = mMessages[position]
        val holder: MessageHolder = holderAction as MessageHolder
        holder.bind(message)
    }

    override fun getItemCount(): Int = mMessages.size

    override fun getItemViewType(position: Int): Int {
        val message = mMessages[position]

        var messageB = Message()
        if (position > 0)
            messageB = mMessages[position - 1]

        return when (message.status) {
            MessageStatus.ACTION -> ACTION
            MessageStatus.INCOMING_UNREAD -> {
                if (!messageB.isMine() && !messageB.isAction()) INCOMING_TYPE
                else INCOMING_FIRST_TYPE
            }
            MessageStatus.INCOMING_READ -> {
                if (!messageB.isMine() && !messageB.isAction()) INCOMING_TYPE
                else INCOMING_FIRST_TYPE
            }
            MessageStatus.OUTGOING_NOT_SENT -> {
                if (messageB.isMine() && !messageB.isAction()) OUTGOING_TYPE
                else OUTGOING_FIRST_TYPE
            }
            MessageStatus.OUTGOING_UNREAD -> {
                if (messageB.isMine() && !messageB.isAction()) OUTGOING_TYPE
                else OUTGOING_FIRST_TYPE
            }
            MessageStatus.OUTGOING_READ -> {
                if (messageB.isMine() && !messageB.isAction()) OUTGOING_TYPE
                else OUTGOING_FIRST_TYPE
            }
        }
    }

    fun changeDataSet(messages: MutableList<Message>) {
        mMessages.clear()
        mMessages.addAll(messages)
        notifyDataSetChanged()
    }
}