package org.chapper.chapper.data.model

import java.util.*

class Chat {
    var id: String = UUID.randomUUID().toString()
    var firstName: String = ""
    var lastName: String = ""
    var username: String = ""
    var bluetoothMacAddress: String = ""
    var messages: List<Message> = arrayListOf()

    val lastMessage: Message
        get() {
            if (messages.isNotEmpty())
                return messages[messages.size - 1]
            return Message()
        }

    val newMessagesNumber: Int
        get() {
            var newCount = 0
            (messages.size - 1 downTo 0)
                    .takeWhile { messages[it].isNew }
                    .forEach { newCount++ }
            return newCount
        }
}