package org.chapper.chapper.domain

import android.app.ActivityManager
import android.content.Context

object Utils {
    fun isForeground(context: Context): Boolean {
        val am = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val tasks = am.runningAppProcesses
        val packageName = context.packageName
        return tasks.any {
            ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND == it.importance
                    && packageName == it.processName
        }
    }
}