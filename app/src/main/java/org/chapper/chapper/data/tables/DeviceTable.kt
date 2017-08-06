package org.chapper.chapper.data.tables

import android.content.ContentValues
import android.database.Cursor
import org.chapper.chapper.data.model.Device
import org.sqlite.database.sqlite.SQLiteDatabase
import ru.arturvasilov.sqlite.core.BaseTable
import ru.arturvasilov.sqlite.core.Table

class DeviceTable : BaseTable<Device>() {
    companion object {
        val TABLE: Table<Device> = DeviceTable()
    }

    override fun onCreate(database: SQLiteDatabase) {
        TODO("not implemented")
    }

    override fun toValues(t: Device): ContentValues {
        TODO("not implemented")
    }

    override fun fromCursor(cursor: Cursor): Device {
        TODO("not implemented")
    }
}