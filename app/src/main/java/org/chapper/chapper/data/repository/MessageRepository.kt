package org.chapper.chapper.data.repository

import com.raizlabs.android.dbflow.kotlinextensions.*
import org.chapper.chapper.data.model.Message
import org.chapper.chapper.data.model.Message_Table
import org.chapper.chapper.data.status.MessageStatus

object MessageRepository {
    fun getMessages(chatId: String): MutableList<Message> =
            (select from Message::class where (Message_Table.chatId eq chatId)).list

    fun receiveMessages(chatId: String) {
        val messages = (select from Message::class
                where (Message_Table.chatId eq chatId)
                and (Message_Table.status eq MessageStatus.OUTGOING_NOT_SENT))
                .list

        for (message in messages) {
            message.status = MessageStatus.OUTGOING_UNREAD
            message.save()
        }
    }

    fun readOutgoingMessages(chatId: String) {
        val messages = (select from Message::class
                where (Message_Table.chatId eq chatId)
                and ((Message_Table.status eq MessageStatus.OUTGOING_UNREAD)
                or (Message_Table.status eq MessageStatus.OUTGOING_NOT_SENT)))
                .list

        for (message in messages) {
            message.status = MessageStatus.OUTGOING_READ
            message.save()
        }
    }

    fun readIncomingMessages(chatId: String) {
        val messages = (select from Message::class
                where (Message_Table.chatId eq chatId)
                and (Message_Table.status eq MessageStatus.INCOMING_UNREAD))
                .list

        for (message in messages) {
            message.status = MessageStatus.INCOMING_READ
            message.save()
        }
    }
}