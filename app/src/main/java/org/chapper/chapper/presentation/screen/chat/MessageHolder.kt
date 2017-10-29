package org.chapper.chapper.presentation.screen.chat

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import kotterknife.bindView
import org.chapper.chapper.R
import org.chapper.chapper.data.model.Message
import org.chapper.chapper.data.repository.ImageRepository
import org.chapper.chapper.data.status.MessageStatus
import org.jetbrains.anko.image

class MessageHolder(val context: Context, itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val mMessageText: TextView by bindView(R.id.message_text)
    private val mMessagePhoto: ImageView by bindView(R.id.message_image)
    private val mMessageTime: TextView by bindView(R.id.message_time)
    private val mMessageStatus: ImageView by bindView(R.id.message_status)

    fun bind(message: Message, listener: ChatAdapter.OnItemClickListener) {
        if (message.photo != "") {
            val bitmap = ImageRepository.getImage(context, message.photo)
            if (bitmap != null) mMessagePhoto.setImageBitmap(bitmap)
            if (!message.isAction()) mMessageTime.text = message.getTimeString()
        } else {
            mMessageText.text = message.text
            if (!message.isAction())
                mMessageTime.text = message.getTimeString()
        }

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

        itemView.setOnClickListener {
            listener.onItemClick(message)
        }
    }
}