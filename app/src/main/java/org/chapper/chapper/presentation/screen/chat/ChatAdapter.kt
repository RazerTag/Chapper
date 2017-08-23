package org.chapper.chapper.presentation.screen.chat

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import org.chapper.chapper.R
import org.chapper.chapper.data.MessageStatus
import org.chapper.chapper.data.model.Message
import kotlin.properties.Delegates

class ChatAdapter(private val mMessages: List<Message>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
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
                return IncomingMessageHolder(itemView)
            }
            INCOMING_FIRST_TYPE -> {
                itemView = LayoutInflater.from(parent!!.context)
                        .inflate(R.layout.item_message_incoming_with_angle, parent, false)
                return IncomingMessageHolder(itemView)
            }
            OUTGOING_TYPE -> {
                itemView = LayoutInflater.from(parent!!.context)
                        .inflate(R.layout.item_message_outgoing, parent, false)
                return OutgoingMessageHolder(itemView)
            }
            OUTGOING_FIRST_TYPE -> {
                itemView = LayoutInflater.from(parent!!.context)
                        .inflate(R.layout.item_message_outgoing_with_angle, parent, false)
                return OutgoingMessageHolder(itemView)
            }
            else -> {
                itemView = LayoutInflater.from(parent!!.context)
                        .inflate(R.layout.item_message_action, parent, false)
                return ActionMessageHolder(itemView)
            }
        }
    }

    override fun onBindViewHolder(holderAction: RecyclerView.ViewHolder?, position: Int) {
        val message = mMessages[position]

        when (getItemViewType(position)) {
            INCOMING_TYPE -> {
                val holder: IncomingMessageHolder = holderAction as IncomingMessageHolder
                holder.bind(message)
            }
            INCOMING_FIRST_TYPE -> {
                val holder: IncomingMessageHolder = holderAction as IncomingMessageHolder
                holder.bind(message)
            }
            OUTGOING_TYPE -> {
                val holder: OutgoingMessageHolder = holderAction as OutgoingMessageHolder
                holder.bind(message)
            }
            OUTGOING_FIRST_TYPE -> {
                val holder: OutgoingMessageHolder = holderAction as OutgoingMessageHolder
                holder.bind(message)
            }
            else -> {
                val holder: ActionMessageHolder = holderAction as ActionMessageHolder
                holder.bind(message)
            }
        }
    }

    override fun getItemCount(): Int = mMessages.size

    override fun getItemViewType(position: Int): Int {
        val message = mMessages[position]
        return when (message.status) {
            MessageStatus.ACTION -> ACTION
            MessageStatus.INCOMING_UNREAD -> {
                if (!mMessages[position - 1].isMine()) INCOMING_TYPE
                else INCOMING_FIRST_TYPE
            }
            MessageStatus.INCOMING_READ -> {
                if (!mMessages[position - 1].isMine()) INCOMING_TYPE
                else INCOMING_FIRST_TYPE
            }
            MessageStatus.OUTGOING_NOT_SENT -> {
                if (mMessages[position - 1].isMine()) OUTGOING_TYPE
                else OUTGOING_FIRST_TYPE
            }
            MessageStatus.OUTGOING_UNREAD -> {
                if (mMessages[position - 1].isMine()) OUTGOING_TYPE
                else OUTGOING_FIRST_TYPE
            }
            MessageStatus.OUTGOING_READ -> {
                if (mMessages[position - 1].isMine()) OUTGOING_TYPE
                else OUTGOING_FIRST_TYPE
            }
        }
    }
}