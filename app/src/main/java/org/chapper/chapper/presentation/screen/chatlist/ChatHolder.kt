package org.chapper.chapper.presentation.screen.chatlist

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import de.hdodenhof.circleimageview.CircleImageView
import kotterknife.bindView
import org.chapper.chapper.R
import org.chapper.chapper.data.model.Chat
import org.chapper.chapper.data.repository.ChatRepository
import org.chapper.chapper.data.repository.ImageRepository
import org.chapper.chapper.data.status.MessageStatus

class ChatHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {
    private val mProfileImageChars: TextView by bindView(R.id.profile_image_chars)
    private val mProfileImage: CircleImageView by bindView(R.id.profile_image)
    private val mChatName: TextView by bindView(R.id.chat_name)
    private val mChatPreview: TextView by bindView(R.id.chat_preview)
    private val mNewMessagesCounter: TextView by bindView(R.id.new_messages_counter)
    private val mLastMessageStatus: ImageView by bindView(R.id.message_status)
    private val mLastMessageTime: TextView by bindView(R.id.last_message_time)

    fun bind(context: Context, chat: Chat, listener: ChatListAdapter.OnItemClickListener) {
        val lastMessage = ChatRepository.getChat(chat.id).getLastMessage()
        mProfileImageChars.text = ChatRepository.getFirstCharsName(chat)

        val photo = ImageRepository.getImage(context, chat.id)
        if (photo != null)
            mProfileImage.setImageBitmap(photo)
        else
            mProfileImage.visibility = View.GONE

        mChatName.text = ChatRepository.getName(chat)
        mChatPreview.text = lastMessage.text

        val newMessages = ChatRepository.getChat(chat.id).getNewMessagesNumber()
        if (newMessages != 0) {
            mNewMessagesCounter.text = newMessages.toString()
            mNewMessagesCounter.visibility = View.VISIBLE
        } else {
            mNewMessagesCounter.visibility = View.GONE
        }

        when (lastMessage.status) {
            MessageStatus.OUTGOING_NOT_SENT -> mLastMessageStatus.setImageResource(R.drawable.clock)
            MessageStatus.OUTGOING_UNREAD -> mLastMessageStatus.setImageResource(R.drawable.check)
            MessageStatus.OUTGOING_READ -> mLastMessageStatus.setImageResource(R.drawable.check_all)
            else -> mLastMessageStatus.visibility = View.GONE
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