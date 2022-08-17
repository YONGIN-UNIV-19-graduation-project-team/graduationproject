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
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class AlarmReceiver : BroadcastReceiver() {
    val CHANNEL_ID = "notification_channel"
    //val NOTIFICATION_ID = 0
    var getTitle=""
    var getTime=""
    var getRequestCode=""
    var setResetTime = ""
    lateinit var notificationManager: NotificationManager
    @RequiresApi(Build.VERSION_CODES.S)
    override fun onReceive(context: Context, intent: Intent) {

        notificationManager = context.getSystemService(
            Context.NOTIFICATION_SERVICE) as NotificationManager
        getTitle = intent.getStringExtra("title").toString()
        getTime = intent.getStringExtra("time").toString()
        getRequestCode = intent.getStringExtra("requestCode").toString()
        //////////요일 로직 구현부///////////
        var weekList = intent.getSerializableExtra("weekList") as ArrayList<Boolean>
        println(weekList)
        if((doDayOfWeek()=="월"&&weekList[0])
            ||(doDayOfWeek()=="화"&&weekList[1])
            ||(doDayOfWeek()=="수"&&weekList[2])
            ||(doDayOfWeek()=="목"&&weekList[3])
            ||(doDayOfWeek()=="금"&&weekList[4])
            ||(doDayOfWeek()=="토"&&weekList[5])
            ||(doDayOfWeek()=="일"&&weekList[6]))
        {
            createNotificationChannel()
            deliverNotification(context)
        }else println("요일이 일치하지 않아 알림이 뜨지 않았다.")
        ///////////////////////////////////////////////
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
    @RequiresApi(Build.VERSION_CODES.S)
    private fun deliverNotification(context: Context){
        val contextIntent = Intent(context,Camera::class.java) //알림 클릭 시 이동하는 인텐트.
        val contentPendingIntent = PendingIntent.getActivity(context, getRequestCode.toInt(),//request Code
            contextIntent,
            PendingIntent.FLAG_MUTABLE or PendingIntent.FLAG_UPDATE_CURRENT//현재 인텐트를 유지하고, 대신 인텐트의 extra data는 새로 전달된 Intent로 교체.
        )
        println("pendingintent 수신 완료")
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

    private fun doDayOfWeek(): String? {//오늘의 요일 구하기
        val cal: Calendar = Calendar.getInstance()
        var strWeek: String? = null
        val nWeek: Int = cal.get(Calendar.DAY_OF_WEEK)

        if (nWeek == 1) {
            strWeek = "일"
        } else if (nWeek == 2) {
            strWeek = "월"
        } else if (nWeek == 3) {
            strWeek = "화"
        } else if (nWeek == 4) {
            strWeek = "수"
        } else if (nWeek == 5) {
            strWeek = "목"
        } else if (nWeek == 6) {
            strWeek = "금"
        } else if (nWeek == 7) {
            strWeek = "토"
        }
        return strWeek
    }
}