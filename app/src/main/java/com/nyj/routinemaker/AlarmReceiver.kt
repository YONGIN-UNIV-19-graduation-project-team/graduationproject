package com.nyj.routinemaker

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.core.app.NotificationCompat
import java.text.SimpleDateFormat
import java.util.*


class AlarmReceiver : BroadcastReceiver() {
    val CHANNEL_ID = "notification_channel"
    //val NOTIFICATION_ID = 0
    var getTitle=""
    var getTime=""
    var getRequestCode=""
    var setResetTime = ""
    lateinit var notificationManager: NotificationManager
    override fun onReceive(context: Context, intent: Intent) {

        notificationManager = context.getSystemService(
            Context.NOTIFICATION_SERVICE) as NotificationManager
        getTitle = intent.getStringExtra("title").toString()
        getTime = intent.getStringExtra("time").toString()
        getRequestCode = intent.getStringExtra("requestCode").toString()


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
            notificationManager?.createNotificationChannel(notificationChannel)
        }
    }
    private fun deliverNotification(context: Context){
        val contextIntent = Intent(context,MainActivity::class.java) //알림 클릭 시 이동하는 인텐트.
        val contentPendingIntent = PendingIntent.getActivity(context, getRequestCode.toInt(),//request Code
            contextIntent,
            PendingIntent.FLAG_UPDATE_CURRENT//현재 인텐트를 유지하고, 대신 인텐트의 extra data는 새로 전달된 Intent로 교체.
        )

        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setContentTitle("제목 :"+getTitle)//제목
            .setContentText("RequestCode : "+getRequestCode)//내용
            .setContentIntent(contentPendingIntent)//푸쉬알람 클릭시 인텐트작업
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)//푸쉬알람 클릭 시 사라짐
            .setDefaults(NotificationCompat.DEFAULT_ALL)//소리나 진동 설정 or 키워드로 둘다 설정 가능
            .build()
        notificationManager?.notify(getRequestCode.toInt(),builder)
        //notificationManager?.cancel(getRequestCode.toInt())
    }

}