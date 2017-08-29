package org.chapper.chapper.data

import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Build

object ResourceGetter {
    fun getDrawable(context: Context, id: Int): Drawable {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            context.resources.getDrawable(id, context.theme)
        } else {
            context.resources.getDrawable(id)
        }
    }

    fun getColor(context: Context, id: Int): Int {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            context.resources.getColor(id, context.theme)
        } else {
            context.resources.getColor(id)
        }
    }
}