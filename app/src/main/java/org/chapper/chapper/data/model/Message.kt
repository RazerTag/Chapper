package org.chapper.chapper.data.model

import android.text.format.DateUtils
import com.raizlabs.android.dbflow.annotation.Column
import com.raizlabs.android.dbflow.annotation.PrimaryKey
import com.raizlabs.android.dbflow.annotation.Table
import com.raizlabs.android.dbflow.annotation.Unique
import com.raizlabs.android.dbflow.structure.BaseModel
import org.chapper.chapper.data.database.AppDatabase
import org.chapper.chapper.data.status.MessageStatus
import org.chapper.chapper.domain.usecase.DateUseCase
import java.text.SimpleDateFormat
import java.util.*

@Table(database = AppDatabase::class)
data class Message(
        @PrimaryKey
        @Unique
        var id: String = UUID.randomUUID().toString(),

        @Column
        var chatId: String = "",

        @Column
        var status: MessageStatus = MessageStatus.INCOMING_UNREAD,

        @Column
        var text: String = "",

        @Column
        var date: Date = Date()
) : BaseModel() {
    fun getTimeString(): String = when {
        DateUtils.isToday(date.time) -> SimpleDateFormat("HH:mm").format(date)

        DateUseCase.isDateInCurrentWeek(date) -> SimpleDateFormat("EEE").format(date)

        else -> SimpleDateFormat("MMM d").format(date)
    }

    fun isMine(): Boolean {
        return (status == MessageStatus.OUTGOING_READ
                || status == MessageStatus.OUTGOING_UNREAD
                || status == MessageStatus.OUTGOING_NOT_SENT)
    }

    fun isAction(): Boolean = status == MessageStatus.ACTION
}