package org.chapper.chapper.data.tables

import android.content.ContentValues
import android.database.Cursor
import org.chapper.chapper.data.model.Chat
import org.sqlite.database.sqlite.SQLiteDatabase
import ru.arturvasilov.sqlite.core.BaseTable
import ru.arturvasilov.sqlite.core.Table

class ChatTable : BaseTable<Chat>() {
    companion object {
        val TABLE: Table<Chat> = ChatTable()
    }

    override fun onCreate(database: SQLiteDatabase) {
        TODO("not implemented")
    }

    override fun toValues(t: Chat): ContentValues {
        TODO("not implemented")
    }

    override fun fromCursor(cursor: Cursor): Chat {
        TODO("not implemented")
    }
}