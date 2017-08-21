package org.chapper.chapper.data.model

import com.raizlabs.android.dbflow.annotation.Column
import com.raizlabs.android.dbflow.annotation.PrimaryKey
import com.raizlabs.android.dbflow.annotation.Table
import com.raizlabs.android.dbflow.annotation.Unique
import com.raizlabs.android.dbflow.structure.BaseModel
import org.chapper.chapper.data.MessageStatus
import org.chapper.chapper.data.database.AppDatabase
import java.util.*

@Table(database = AppDatabase::class)
data class Message(
        @PrimaryKey(autoincrement = true)
        @Unique
        var id: Int = 0,

        @Column
        var chatId: String = "",

        @Column
        var status: MessageStatus = MessageStatus.INCOMING_UNREAD,

        @Column
        var text: String = "",

        @Column
        var date: Date = Date()
) : BaseModel() {
    val timeString = ""
}