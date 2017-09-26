package org.chapper.chapper.data.repository

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import java.io.ByteArrayOutputStream
import java.io.FileInputStream
import java.io.FileOutputStream
import kotlin.properties.Delegates


object ImageRepository {
    fun saveImage(context: Context, chatId: String, bitmap: Bitmap) {
        var fos: FileOutputStream by Delegates.notNull()
        try {
            fos = context.openFileOutput(chatId, Context.MODE_PRIVATE)
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos)
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            fos.close()
        }
    }

    fun getImage(context: Context, chatId: String): Bitmap? {
        var b: Bitmap? = null
        var fis: FileInputStream? = null
        try {
            fis = context.openFileInput(chatId)
            b = BitmapFactory.decodeStream(fis)
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            try {
                fis!!.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return b
    }

    fun bitmapToJson(bitmap: Bitmap): String {
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos)
        val b = baos.toByteArray()
        return Base64.encodeToString(b, Base64.DEFAULT)
    }

    fun jsonToBitmap(encodedString: String): Bitmap? {
        return try {
            val encodeByte = Base64.decode(encodedString, Base64.DEFAULT)
            BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.size)
        } catch (e: Exception) {
            e.message
            null
        }

    }
}