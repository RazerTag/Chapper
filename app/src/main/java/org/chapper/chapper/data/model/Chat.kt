package org.chapper.chapper.data.model

import android.content.Context
import android.text.format.DateUtils
import com.raizlabs.android.dbflow.annotation.Column
import com.raizlabs.android.dbflow.annotation.PrimaryKey
import com.raizlabs.android.dbflow.annotation.Table
import com.raizlabs.android.dbflow.annotation.Unique
import com.raizlabs.android.dbflow.kotlinextensions.*
import org.chapper.chapper.R
import org.chapper.chapper.data.database.AppDatabase
import org.chapper.chapper.data.repository.MessageRepository
import org.chapper.chapper.data.status.MessageStatus
import org.chapper.chapper.domain.usecase.DateUseCase
import java.text.SimpleDateFormat
import java.util.*

@Table(database = AppDatabase::class)
data class Chat(
        @PrimaryKey
        @Unique
        var id: String = UUID.randomUUID().toString(),

        @Column
        var photo: String = "",

        @Column
        var firstName: String = "",

        @Column
        var lastName: String = "",

        @Column
        var username: String = "",

        @Column
        var bluetoothMacAddress: String = "",

        @Column
        var lastConnection: Date = Date()
) {
    fun getLastMessage(): Message = MessageRepository.getMessages(id).lastOrNull() ?: Message()
    fun getNewMessagesNumber(): Int = (select from Message::class
            where (Message_Table.chatId eq id)
            and (Message_Table.status eq MessageStatus.INCOMING_UNREAD))
            .list.size

    fun getLastConnectionString(context: Context): String = when {
        DateUtils.isToday(lastConnection.time) -> "${context.getString(R.string.last_connection_in)} ${SimpleDateFormat("HH:mm").format(lastConnection)}"

        DateUseCase.isDateInCurrentWeek(lastConnection) -> "${context.getString(R.string.last_connection_in)} ${SimpleDateFormat("EEE").format(lastConnection)}"

        else -> "${context.getString(R.string.last_connection)} ${SimpleDateFormat("d MMM").format(lastConnection)}"
    }
}