package org.chapper.chapper.data.repository

import com.raizlabs.android.dbflow.kotlinextensions.*
import org.chapper.chapper.data.model.Message
import org.chapper.chapper.data.model.Message_Table

object MessageRepository {
    fun getMessages(chatId: Int): List<Message> =
            (select from Message::class where (Message_Table.chatId eq chatId)).list
}