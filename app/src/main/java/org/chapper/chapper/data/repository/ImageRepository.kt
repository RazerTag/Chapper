package org.chapper.chapper.data.repository

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Bitmap.CompressFormat
import android.graphics.BitmapFactory
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

    fun bitmapToByteArray(bitmap: Bitmap): ByteArray {
        val blob = ByteArrayOutputStream()
        bitmap.compress(CompressFormat.PNG, 0 /* Ignored for PNGs */, blob)
        return blob.toByteArray()
    }

    fun byteArrayToBitmap(byteArray: ByteArray): Bitmap? {
        try {
            return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }
}