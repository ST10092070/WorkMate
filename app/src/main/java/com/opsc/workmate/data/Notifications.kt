package com.opsc.workmate.data

import android.Manifest
import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationCompat.VISIBILITY_SECRET
import androidx.core.app.NotificationManagerCompat
import com.opsc.workmate.MainActivity

object Notifications {
    @SuppressLint("SuspiciousIndentation")
    fun simpleNotification(context: Context?, textTitle: String, textContent: String) {
        val notificationId = 12313
        val builder = context?.let {
            NotificationCompat.Builder(it, NotificationChannel.CHANNEL1)
                .setSmallIcon(com.google.accompanist.permissions.R.drawable.notification_bg)
                .setContentTitle(textTitle)
                .setContentIntent(addContentIntent(context))
                .setContentText(textContent)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setVisibility(VISIBILITY_SECRET)
                .setAutoCancel(true)
        }

        val notificationManagerCompat = context?.let { NotificationManagerCompat.from(it) }

        if (context?.let {
                ActivityCompat.checkSelfPermission(
                    it,
                    Manifest.permission.POST_NOTIFICATIONS
                )
            } != PackageManager.PERMISSION_GRANTED
        )

            if (notificationManagerCompat != null) {
                if (builder != null) {
                    notificationManagerCompat.notify(notificationId, builder.build())
                }
            }
    }

    private fun addContentIntent(context: Context?): PendingIntent {
        val intent = Intent(context, MainActivity::class.java)
        return PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }
}