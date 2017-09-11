package org.chapper.chapper.presentation.screen.chatlist

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import butterknife.bindView
import org.chapper.chapper.R
import org.chapper.chapper.data.model.Chat
import org.chapper.chapper.data.repository.ChatRepository
import org.chapper.chapper.data.status.MessageStatus

class ChatHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {
    private val mProfileImage: TextView by bindView(R.id.profile_image)
    private val mChatName: TextView by bindView(R.id.chat_name)
    private val mChatPreview: TextView by bindView(R.id.chat_preview)
    private val mNewMessagesCounter: TextView by bindView(R.id.new_messages_counter)
    private val mLastMessageStatus: ImageView by bindView(R.id.message_status)
    private val mLastMessageTime: TextView by bindView(R.id.last_message_time)

    fun bind(chat: Chat, listener: ChatListAdapter.OnItemClickListener) {
        val lastMessage = ChatRepository.getChat(chat.id).getLastMessage()
        // TODO: Make images with Glide
        mProfileImage.text = ChatRepository.getFirstCharsName(chat)

        mChatName.text = ChatRepository.getName(chat)
        mChatPreview.text = lastMessage.text

        val newMessages = ChatRepository.getChat(chat.id).getNewMessagesNumber()
        if (newMessages != 0) {
            mNewMessagesCounter.text = newMessages.toString()
            mNewMessagesCounter.visibility = View.VISIBLE
        } else {
            mNewMessagesCounter.visibility = View.INVISIBLE
        }

        when (lastMessage.status) {
            MessageStatus.OUTGOING_NOT_SENT -> mLastMessageStatus.setImageResource(R.drawable.clock)
            MessageStatus.OUTGOING_UNREAD -> mLastMessageStatus.setImageResource(R.drawable.check)
            MessageStatus.OUTGOING_READ -> mLastMessageStatus.setImageResource(R.drawable.check_all)
            else -> mLastMessageStatus.visibility = View.INVISIBLE
        }

        mLastMessageTime.text = lastMessage.getTimeString()

        itemView.setOnClickListener {
            listener.onItemClick(chat)
        }

        itemView.setOnLongClickListener {
            listener.onItemLongClick(chat)
        }
    }
}