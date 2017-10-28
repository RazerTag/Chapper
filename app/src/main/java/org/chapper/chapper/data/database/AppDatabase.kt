package org.chapper.chapper.data.database

import com.raizlabs.android.dbflow.annotation.Database
import com.raizlabs.android.dbflow.annotation.Migration
import com.raizlabs.android.dbflow.sql.SQLiteType
import com.raizlabs.android.dbflow.sql.migration.AlterTableMigration
import org.chapper.chapper.data.model.Chat
import org.chapper.chapper.data.model.Message
import org.chapper.chapper.data.model.Settings

@Database(name = AppDatabase.NAME, version = AppDatabase.VERSION)
class AppDatabase {
    companion object {
        const val NAME = "Chapper"
        const val VERSION = 5
    }

    @Migration(database = AppDatabase::class, version = 5)
    class Migration5Message : AlterTableMigration<Message>(Message::class.java) {
        override fun onPreMigrate() {
            addColumn(SQLiteType.TEXT, "photo")
        }
    }

    @Migration(database = AppDatabase::class, version = 4)
    class Migration4Chat : AlterTableMigration<Chat>(Chat::class.java) {
        override fun onPreMigrate() {
            addColumn(SQLiteType.TEXT, "photoId")
        }
    }

    @Migration(database = AppDatabase::class, version = 4)
    class Migration4Settings : AlterTableMigration<Settings>(Settings::class.java) {
        override fun onPreMigrate() {
            addColumn(SQLiteType.TEXT, "photoId")
        }
    }

    @Migration(database = AppDatabase::class, version = 3)
    class Migration3 : AlterTableMigration<Settings>(Settings::class.java) {
        override fun onPreMigrate() {
            addColumn(SQLiteType.INTEGER, "isSendByEnter")
        }
    }

    @Migration(database = AppDatabase::class, version = 2)
    class Migration2 : AlterTableMigration<Chat>(Chat::class.java) {
        override fun onPreMigrate() {
            addColumn(SQLiteType.TEXT, "lastConnection")
        }
    }
}