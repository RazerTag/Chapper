package org.chapper.chapper.data.sqlite

import org.chapper.chapper.data.tables.DeviceTable
import ru.arturvasilov.sqlite.core.SQLiteConfig
import ru.arturvasilov.sqlite.core.SQLiteContentProvider
import ru.arturvasilov.sqlite.core.SQLiteSchema

class ChapperContentProvider : SQLiteContentProvider() {
    private val DATABASE_NAME = "simpleweather.db"
    private val CONTENT_AUTHORITY = "ru.gdgkazan.simpleweather"

    override fun prepareConfig(config: SQLiteConfig) {
        config.setDatabaseName(DATABASE_NAME)
        config.setAuthority(CONTENT_AUTHORITY)
    }

    override fun prepareSchema(schema: SQLiteSchema) {
        schema.register(DeviceTable.TABLE)
    }
}