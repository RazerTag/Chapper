package org.chapper.chapper.data.model

import org.chapper.chapper.data.MessageStatus
import java.text.SimpleDateFormat
import java.util.*

class Message {
    var status = MessageStatus.INCOMING_UNREAD
    var text = ""
    var date = Date()

    val timeString: String
        get() = SimpleDateFormat("HH:mm").format(date)

    val isNew: Boolean
        get() = status == MessageStatus.INCOMING_UNREAD

    val isMine: Boolean
        get() = status == MessageStatus.OUTGOING_NOT_SENT
                || status == MessageStatus.OUTGOING_UNREAD
                || status == MessageStatus.OUTGOING_READ
}