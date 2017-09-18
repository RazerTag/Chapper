package org.chapper.chapper.domain.usecase

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.support.v7.app.NotificationCompat
import org.chapper.chapper.R
import org.chapper.chapper.data.Constants
import org.chapper.chapper.presentation.screen.chat.ChatActivity

object NotificationUseCase {
    fun sendNotification(context: Context, chatId: String, title: String, text: String) {
        val notificationIntent = Intent(context, ChatActivity::class.java)
        notificationIntent.putExtra(Constants.CHAT_ID_EXTRA, chatId)

        val contentIntent = PendingIntent.getActivity(context,
                0, notificationIntent,
                PendingIntent.FLAG_CANCEL_CURRENT)

        val mBuilder = NotificationCompat.Builder(context)
                .setContentIntent(contentIntent)
                .setSmallIcon(R.drawable.message)
                .setContentTitle(title)
                .setContentText(text)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setPriority(Notification.PRIORITY_HIGH)

        val notificationManager = context
                .getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(12, mBuilder.build())
    }

    fun cleatAll(context: Context) {
        val nMgr = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        nMgr.cancelAll()
    }
}