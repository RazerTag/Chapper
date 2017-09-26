package org.chapper.chapper.data.repository

import com.raizlabs.android.dbflow.kotlinextensions.*
import org.chapper.chapper.data.model.Chat
import org.chapper.chapper.data.model.Chat_Table

object ChatRepository {
    fun getChatsSorted(): MutableList<Chat> {
        val chats = getChats()

        return chats
                .sortedByDescending { it.getLastMessage().date }
                .toMutableList()
    }

    private fun getChats(): MutableList<Chat> = (select from Chat::class).list

    fun getChat(chatId: String): Chat =
            (select from Chat::class where (Chat_Table.id eq chatId)).querySingle() ?: Chat()

    fun getChat(name: String, address: String): Chat =
            (select from Chat::class
                    where (Chat_Table.username eq name)
                    and (Chat_Table.bluetoothMacAddress eq address))
                    .querySingle() ?: Chat()

    fun contains(address: String): Boolean =
            (select from Chat::class where (Chat_Table.bluetoothMacAddress eq address))
                    .list.size != 0

    fun getName(chat: Chat): String {
        return if (chat.firstName.isEmpty() || chat.lastName.isEmpty()) {
            chat.username
        } else {
            "${chat.firstName} ${chat.lastName}"
        }
    }

    fun getFirstCharsName(chat: Chat): String {
        return try {
            if (chat.firstName.isNotEmpty() && chat.lastName.isNotEmpty()) {
                "${chat.firstName[0]}${chat.lastName[0]}"
            } else {
                "${chat.username[0]}"
            }
        } catch (e: Exception) {
            "..."
        }
    }
}