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
import androidx.room.Room
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class AlarmReceiver : BroadcastReceiver() {
    //채널 생성을 위한 채널ID 초기화
    val CHANNEL_ID = "notification_channel"

    //루틴 제목,시간,id(RequestCode)을 intent로 받아오기 위한 String변수 초기화
    var getTitle=""
    var getTime=""
    //알림은 RequestCode로 구분 가능하다. 그래서 Routine DB의 id값을 RequestCode로 설정했다
    var getRequestCode=""

    lateinit var notificationManager: NotificationManager

    //알림을 수신 받았을때의 코드(onReceive)
    @RequiresApi(Build.VERSION_CODES.S)
    override fun onReceive(context: Context, intent: Intent) {

        //intent로 제목,시간,RequestCode 수신받음
        notificationManager = context.getSystemService(
            Context.NOTIFICATION_SERVICE) as NotificationManager
        getTitle = intent.getStringExtra("title").toString()
        getTime = intent.getStringExtra("time").toString()
        getRequestCode = intent.getStringExtra("requestCode").toString()

        val db = Room.databaseBuilder(
            context.applicationContext,AppDatabase::class.java,"routine_databases"
        ).allowMainThreadQueries().build()

        //////////요일 로직 구현부///////////
        //수신받은 루틴의 요일이 담긴 리스트를 intent로 가져와서 weekList에 저장한다
        var weekList = intent.getSerializableExtra("weekList") as ArrayList<Boolean>

        //오늘의 요일과 weekList의 요일을 비교해서 오늘의 루틴인지 확인한다
        if((doDayOfWeek()=="월"&&weekList[0])
            ||(doDayOfWeek()=="화"&&weekList[1])
            ||(doDayOfWeek()=="수"&&weekList[2])
            ||(doDayOfWeek()=="목"&&weekList[3])
            ||(doDayOfWeek()=="금"&&weekList[4])
            ||(doDayOfWeek()=="토"&&weekList[5])
            ||(doDayOfWeek()=="일"&&weekList[6]))
        {
            //오늘이 루틴을 수행하는 요일이면 채널생성과 Notification 전송.
            //만들었다가 삭제한 루틴일 수도 있으니 수신받은 RequestCode가 db의 id 중에 있는지 확인한다.
            if(db.routine_DAO().getIdExist(getRequestCode.toLong())==1) {
                createNotificationChannel()
                deliverNotification(context)
            }else println("삭제된 루틴이라 알림이 뜨지 않았다.")
        }else println("요일이 일치하지 않아 알림이 뜨지 않았다.")
        db.close()
        ///////////////////////////////////////////////
    }

    //수신받은 루틴알람이 오늘 해야할 알람이면 이 함수가 수행된다.
    fun createNotificationChannel(){
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            //채널생성
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

    //알림창 수행하는 코드
    @RequiresApi(Build.VERSION_CODES.S)
    private fun deliverNotification(context: Context){
        System.currentTimeMillis()
        val sendTime = "${System.currentTimeMillis()}" //현재 시간(알림 클릭한 시간)을 인텐트로 카메라엑티비티에 전달.
        val contextIntent = Intent(context,Camera::class.java) //알림 클릭 시 이동하는 인텐트. 알림 클릭 시 카메라 액티비티로 이동한다.
        contextIntent.putExtra("id",getRequestCode)
        contextIntent.putExtra("time",sendTime)
        val contentPendingIntent = PendingIntent.getActivity(context, getRequestCode.toInt(),//request Code
            contextIntent,
            PendingIntent.FLAG_MUTABLE or PendingIntent.FLAG_UPDATE_CURRENT//현재 인텐트를 유지하고, 대신 인텐트의 extra data는 새로 전달된 Intent로 교체.
        )

        //println("pendingintent 수신 완료")
        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.mipmap.icon)
            .setContentTitle("루틴 알림")//제목
            .setContentText("오늘도 <"+getTitle+"> 루틴을 수행하세요!")//내용
            .setContentIntent(contentPendingIntent)//푸쉬알람 클릭시 인텐트작업
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)//푸쉬알람 클릭 시 사라짐
            .setDefaults(NotificationCompat.DEFAULT_ALL)//소리나 진동 설정 or 키워드로 둘다 설정 가능
            .build()
        notificationManager?.notify(getRequestCode.toInt(),builder)
    }

    //오늘의 요일 구하는 함수
    private fun doDayOfWeek(): String? {
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