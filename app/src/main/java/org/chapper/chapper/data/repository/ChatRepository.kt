package org.chapper.chapper.data.repository

import com.raizlabs.android.dbflow.kotlinextensions.*
import org.chapper.chapper.data.model.Chat
import org.chapper.chapper.data.model.Chat_Table

object ChatRepository {
    fun getChats(): List<Chat> = (select from Chat::class).list

    fun getChat(chatId: Int): Chat =
            (select from Chat::class where (Chat_Table.id eq chatId)).querySingle() ?: Chat()
}