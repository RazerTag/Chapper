package org.chapper.chapper.data.repository

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import kotlin.properties.Delegates

object ImageRepository {
    fun saveImage(context: Context, chatId: String, bitmap: Bitmap) {
        var fos: FileOutputStream by Delegates.notNull()
        try {
            fos = context.openFileOutput(chatId, Context.MODE_PRIVATE)
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos)
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
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
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
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
}