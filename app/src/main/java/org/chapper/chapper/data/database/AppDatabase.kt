package org.chapper.chapper.data.database

import com.raizlabs.android.dbflow.annotation.Database

@Database(name = AppDatabase.NAME, version = AppDatabase.VERSION)
class AppDatabase {
    companion object {
        const val NAME = "Chapper"
        const val VERSION = 1
    }
}