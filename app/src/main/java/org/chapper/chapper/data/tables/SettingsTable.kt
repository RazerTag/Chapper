package org.chapper.chapper.data.tables

import android.content.ContentValues
import android.database.Cursor
import org.chapper.chapper.data.model.Settings
import org.sqlite.database.sqlite.SQLiteDatabase
import ru.arturvasilov.sqlite.core.BaseTable
import ru.arturvasilov.sqlite.core.SQLite
import ru.arturvasilov.sqlite.core.Table
import ru.arturvasilov.sqlite.utils.TableBuilder

class SettingsTable : BaseTable<Settings>() {
    companion object {
        val TABLE: Table<Settings> = SettingsTable()

        val SETTINGS: Settings
            get() = SQLite.get().querySingle(SettingsTable.TABLE)!!

        val IS_FIRST_START: String = "is_first_start"
        val FIRST_NAME: String = "first_name"
        val LAST_NAME: String = "last_name"
    }

    override fun onCreate(database: SQLiteDatabase) {
        TableBuilder.create(this)
                .intColumn(IS_FIRST_START)
                .textColumn(FIRST_NAME)
                .textColumn(LAST_NAME)
                .execute(database)
    }

    override fun toValues(t: Settings): ContentValues {
        val values = ContentValues()
        values.put(IS_FIRST_START, t.isFirstStart)
        values.put(FIRST_NAME, t.firstName)
        values.put(LAST_NAME, t.lastName)
        return values
    }

    override fun fromCursor(cursor: Cursor): Settings {
        val settings = Settings()
        settings.isFirstStart = cursor.getInt(cursor.getColumnIndex(IS_FIRST_START)) != 0
        settings.firstName = cursor.getString(cursor.getColumnIndex(FIRST_NAME))
        settings.lastName = cursor.getString(cursor.getColumnIndex(LAST_NAME))
        return settings
    }
}