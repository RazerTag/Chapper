package org.chapper.chapper

import android.app.Application
import ru.arturvasilov.sqlite.core.SQLite

class ChapperApp : Application() {
    override fun onCreate() {
        super.onCreate()
        SQLite.initialize(this)
    }
}