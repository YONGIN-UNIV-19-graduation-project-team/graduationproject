package com.nyj.routinemaker

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room
import java.util.*
import kotlin.collections.ArrayList

//정각시 받는 pendingintent
class ResetCheckboxReceiver : BroadcastReceiver() {
    var RoutineList = arrayListOf<Routine>(
        Routine(0L,"더미","3","10",
            true,true,true,true,true,true,true,false)
    )
    override fun onReceive(context: Context, intent: Intent) {
        //Room DB연동
        val db = Room.databaseBuilder(
            context.applicationContext,AppDatabase::class.java,"databases"
        ).allowMainThreadQueries().build()
        //체크박스를 모두 해제하는 쿼리문 호출
        db.routine_DAO().resetcheckbox()
        RoutineList = db.routine_DAO().getAll().toTypedArray().toCollection(ArrayList<Routine>())
        db.close()
        ////////////////////////////////////////

        val alarmManager = context.getSystemService(AppCompatActivity.ALARM_SERVICE) as AlarmManager
        RoutineList.forEach { Routine ->
            var weekList = arrayListOf<Boolean>(Routine.mon,Routine.tue,Routine.wed,Routine.thu,Routine.fri,Routine.sat,Routine.sun)

            val requestCode = Routine.id

            val triggerTime : Calendar = Calendar.getInstance()
            triggerTime.timeInMillis = System.currentTimeMillis()
            triggerTime.set(Calendar.HOUR_OF_DAY,Routine.hour.toInt())
            println("시"+Routine.hour.toInt()+" 분"+Routine.min+" 이름"+Routine.name)
            triggerTime.set(Calendar.MINUTE,Routine.min.toInt())
            triggerTime.set(Calendar.SECOND,0)
            var convertTime = triggerTime.timeInMillis
            var interval:Long = 1000*60*60*24
            val intent = Intent(context,AlarmReceiver::class.java)

            intent.putExtra("title",(Routine.name))
            intent.putExtra("time",(convertTime.toString()))
            intent.putExtra("requestCode",requestCode.toString())
            intent.putExtra("weekList",weekList)

            //val repeatInterval = AlarmManager.INTERVAL_DAY//반복시간설정

            println("캘린더ㅡ를 이용한 밀리초"+convertTime)
            if(System.currentTimeMillis()>convertTime){
                convertTime+=interval
            }
            val pendingIntent = PendingIntent.getBroadcast(
                context,
                requestCode.toInt(),
                intent,
                PendingIntent.FLAG_MUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
            )
            println("pendingintent 전송 완료")
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,convertTime,pendingIntent)





        //////////////////////////////////////////
    }
}
}