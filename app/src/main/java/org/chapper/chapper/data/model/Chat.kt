package org.chapper.chapper.data.model

import com.raizlabs.android.dbflow.annotation.Column
import com.raizlabs.android.dbflow.annotation.PrimaryKey
import com.raizlabs.android.dbflow.annotation.Table
import com.raizlabs.android.dbflow.annotation.Unique
import org.chapper.chapper.data.database.AppDatabase

@Table(database = AppDatabase::class)
data class Chat(
        @PrimaryKey(autoincrement = true)
        @Unique
        var id: Int = 0,

        @Column
        var photo: String = "",

        @Column
        var firstName: String = "",

        @Column
        var lastName: String = "",

        @Column
        var username: String = "",

        @Column
        var bluetoothMacAddress: String = ""
) {
    val lastMessage = Message()
    val newMessagesNumber = 0
}