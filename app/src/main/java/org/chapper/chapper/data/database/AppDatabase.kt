package org.chapper.chapper.data.database

import com.raizlabs.android.dbflow.annotation.Database
import com.raizlabs.android.dbflow.annotation.Migration
import com.raizlabs.android.dbflow.sql.SQLiteType
import com.raizlabs.android.dbflow.sql.migration.AlterTableMigration
import org.chapper.chapper.data.model.Chat

@Database(name = AppDatabase.NAME, version = AppDatabase.VERSION)
class AppDatabase {
    companion object {
        const val NAME = "Chapper"
        const val VERSION = 2
    }

    @Migration(database = AppDatabase::class, version = 2)
    class Migration2 : AlterTableMigration<Chat>(Chat::class.java) {
        override fun onPreMigrate() {
            addColumn(SQLiteType.TEXT, "lastConnection")
        }
    }
}