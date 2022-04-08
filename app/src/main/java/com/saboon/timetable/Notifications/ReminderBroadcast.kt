package com.saboon.timetable.Notifications

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.saboon.timetable.R



const val notificationID = 200
const val channelID = "channelTimeTable"
const val titleExtra = "Time Table"
const val messageExtra = "ders basladi"


class ReminderBroadcast : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        val notificationBuilder = NotificationCompat.Builder(context!!, channelID)
            .setSmallIcon(R.drawable.ic_baseline_add_circle_outline_24)
            .setContentTitle(titleExtra)
            .setContentText(messageExtra)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .build()


        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.notify(notificationID, notificationBuilder)
    }
}