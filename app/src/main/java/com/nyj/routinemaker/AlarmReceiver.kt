package com.nyj.routinemaker

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.core.app.NotificationCompat


class AlarmReceiver : BroadcastReceiver() {
    val CHANNEL_ID = "notification_channel"
    val NOTIFICATION_ID = 0
    lateinit var notificationManager: NotificationManager
    override fun onReceive(context: Context, intent: Intent) {

        notificationManager = context.getSystemService(
            Context.NOTIFICATION_SERVICE) as NotificationManager

        createNotificationChannel()
        deliverNotification(context)

    }
    fun createNotificationChannel(){
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            val notificationChannel = NotificationChannel(
                CHANNEL_ID, "채널 이름입니다",NotificationManager.IMPORTANCE_HIGH
            )
            notificationChannel.enableLights(true)
            notificationChannel.lightColor= Color.RED
            notificationChannel.enableVibration(true)
            notificationChannel.description = "채널의 상세정보입니다."
            notificationManager.createNotificationChannel(notificationChannel)
        }
    }
    private fun deliverNotification(context: Context){
        val contextIntent = Intent(context,MainActivity::class.java) //알림 클릭 시 이동하는 인텐트.
        val contentPendingIntent = PendingIntent.getActivity(
            context,
            NOTIFICATION_ID,//request Code
            contextIntent,
            PendingIntent.FLAG_UPDATE_CURRENT//현재 인텐트를 유지하고, 대신 인텐트의 extra data는 새로 전달된 Intent로 교체.
        )




        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setContentTitle("제목 :")//제목
            .setContentText("내용 입니다.")//내용
            .setContentIntent(contentPendingIntent)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
        notificationManager.notify(System.currentTimeMillis().toInt(),builder.build())
    }

}