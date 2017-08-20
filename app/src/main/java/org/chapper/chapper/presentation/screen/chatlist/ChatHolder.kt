package org.chapper.chapper.presentation.screen.chatlist

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import butterknife.bindView
import de.hdodenhof.circleimageview.CircleImageView
import org.chapper.chapper.R
import org.chapper.chapper.data.model.Chat
import org.chapper.chapper.data.table.ChatTable

class ChatHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {
    val mProfileImage: CircleImageView by bindView(R.id.profile_image)
    val mChatName: TextView by bindView(R.id.chat_name)
    val mChatPreview: TextView by bindView(R.id.chat_preview)
    val mNewMessagesCounter: TextView by bindView(R.id.new_messages_counter)
    val mLastMessageTime: TextView by bindView(R.id.last_message_time)

    fun bind(chat: Chat) {
        // TODO: Make images with Glide
        mProfileImage.setImageResource(R.drawable.account_black)

        mChatName.text = "${chat.firstName} ${chat.lastName}"
        mChatPreview.text = ChatTable.getChat(chat.id)!!.lastMessage.text

        val newMessages = ChatTable.getChat(chat.id)!!.newMessagesNumber
        if (newMessages != 0) {
            mNewMessagesCounter.text = newMessages.toString()
            mNewMessagesCounter.visibility = View.VISIBLE
        } else {
            mNewMessagesCounter.visibility = View.INVISIBLE
        }

        mLastMessageTime.text = ChatTable.getChat(chat.id)!!.lastMessage.timeString
    }
}