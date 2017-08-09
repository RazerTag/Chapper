package org.chapper.chapper.data.tables

import android.content.ContentValues
import android.database.Cursor
import org.chapper.chapper.data.model.Chat
import org.sqlite.database.sqlite.SQLiteDatabase
import ru.arturvasilov.sqlite.core.BaseTable
import ru.arturvasilov.sqlite.core.Table
import ru.arturvasilov.sqlite.utils.TableBuilder

class ChatTable : BaseTable<Chat>() {
    companion object {
        val TABLE: Table<Chat> = ChatTable()

        val FIRST_NAME: String = "first_name"
        val LAST_NAME: String = "last_name"
        val USERNAME: String = "username"
        val BLUETOOTH_MAC_ADDRESS: String = "bluetooth_mac_address"
    }

    override fun onCreate(database: SQLiteDatabase) {
        TableBuilder.create(this)
                .textColumn(FIRST_NAME)
                .textColumn(LAST_NAME)
                .textColumn(USERNAME)
                .textColumn(BLUETOOTH_MAC_ADDRESS)
                .execute(database)
    }

    override fun toValues(t: Chat): ContentValues {
        val values = ContentValues()
        values.put(FIRST_NAME, t.firstName)
        values.put(LAST_NAME, t.lastName)
        values.put(USERNAME, t.username)
        values.put(BLUETOOTH_MAC_ADDRESS, t.bluetoothMacAddress)
        return values
    }

    override fun fromCursor(cursor: Cursor): Chat {
        TODO("not implemented")
    }
}