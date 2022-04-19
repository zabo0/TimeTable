package com.saboon.timetable.Notifications

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.Intent.getIntent
import android.content.Intent.getIntentOld
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.saboon.timetable.Activities.MainActivity
import com.saboon.timetable.R




class ReminderBroadcast : BroadcastReceiver() {


    // TODO: burada diger tarftan gelen parametlerle degistir
    //message extra = yarim saat sonra su ders baslayacak gibisinden
    var notificationID = 0
    var channelID = "HaftalikDersProgrami"
    var titleExtra = ""
    var messageExtra = ""



    override fun onReceive(context: Context?, intent: Intent?) {

        if (intent != null) {
            notificationID = intent.getIntExtra("notificationID",0)
            titleExtra = intent.getStringExtra("TitleExtra").toString()
            messageExtra = intent.getStringExtra("MessageExtra").toString()
        }

        val notificationBuilder = NotificationCompat.Builder(context!!, channelID)
            .setSmallIcon(R.drawable.ic_baseline_add_circle_outline_24)
            .setContentTitle(titleExtra)
            .setContentText(messageExtra)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .build()


        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (notificationID != 0){
            manager.notify(notificationID, notificationBuilder)
        }

    }
}