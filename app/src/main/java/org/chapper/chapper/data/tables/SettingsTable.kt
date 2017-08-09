package org.chapper.chapper.data.tables

import android.content.ContentValues
import android.database.Cursor
import org.chapper.chapper.data.model.Settings
import org.sqlite.database.sqlite.SQLiteDatabase
import ru.arturvasilov.sqlite.core.BaseTable
import ru.arturvasilov.sqlite.core.Table

class SettingsTable : BaseTable<Settings>() {
    companion object {
        val TABLE: Table<Settings> = SettingsTable()
    }

    override fun onCreate(database: SQLiteDatabase) {
        TODO("not implemented")
    }

    override fun toValues(t: Settings): ContentValues {
        TODO("not implemented")
    }

    override fun fromCursor(cursor: Cursor): Settings {
        TODO("not implemented")
    }

}