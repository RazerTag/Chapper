package org.chapper.chapper.data.sqlite

import org.chapper.chapper.data.tables.ChatTable
import org.chapper.chapper.data.tables.SettingsTable
import ru.arturvasilov.sqlite.core.SQLiteConfig
import ru.arturvasilov.sqlite.core.SQLiteContentProvider
import ru.arturvasilov.sqlite.core.SQLiteSchema

class ChapperContentProvider : SQLiteContentProvider() {
    private val DATABASE_NAME = "chapper.db"
    private val CONTENT_AUTHORITY = "org.chapper.chapper"

    override fun prepareConfig(config: SQLiteConfig) {
        config.setDatabaseName(DATABASE_NAME)
        config.setAuthority(CONTENT_AUTHORITY)
    }

    override fun prepareSchema(schema: SQLiteSchema) {
        schema.register(SettingsTable.TABLE)
        schema.register(ChatTable.TABLE)
    }
}