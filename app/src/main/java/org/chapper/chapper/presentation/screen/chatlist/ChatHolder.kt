package org.chapper.chapper.presentation.screen.chatlist

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import butterknife.bindView
import de.hdodenhof.circleimageview.CircleImageView
import org.chapper.chapper.R
import org.chapper.chapper.data.model.Chat
import org.chapper.chapper.data.repository.ChatRepository

class ChatHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {
    private val mProfileImage: CircleImageView by bindView(R.id.profile_image)
    private val mChatName: TextView by bindView(R.id.chat_name)
    private val mChatPreview: TextView by bindView(R.id.chat_preview)
    private val mNewMessagesCounter: TextView by bindView(R.id.new_messages_counter)
    private val mLastMessageTime: TextView by bindView(R.id.last_message_time)

    fun bind(chat: Chat, listener: ChatListAdapter.OnItemClickListener) {
        // TODO: Make images with Glide
        mProfileImage.setImageResource(R.drawable.account_grey)

        mChatName.text = ChatRepository.getName(chat)
        mChatPreview.text = ChatRepository.getChat(chat.id).getLastMessage().text

        val newMessages = ChatRepository.getChat(chat.id).getNewMessagesNumber()
        if (newMessages != 0) {
            mNewMessagesCounter.text = newMessages.toString()
            mNewMessagesCounter.visibility = View.VISIBLE
        } else {
            mNewMessagesCounter.visibility = View.INVISIBLE
        }

        mLastMessageTime.text = ChatRepository.getChat(chat.id).getLastMessage().getTimeString()

        itemView.setOnClickListener {
            listener.onItemClick(chat)
        }
    }
}