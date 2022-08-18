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

        println("정각이 됐다!!!!!!!!!!!!!!!!!!!!1")
        val db = Room.databaseBuilder(
            context.applicationContext,AppDatabase::class.java,"routine_database"
        ).allowMainThreadQueries().build()
        //체크박스를 모두 해제하는 쿼리문 호출
        println("db접근완료")
        db.routine_DAO().resetCheckBox()
        println("체크박스 리셋 시도 완료")

        RoutineList = db.routine_DAO().getAll().toTypedArray().toCollection(ArrayList<Routine>())
        db.close()
        println(RoutineList)
        println("루틴리스트에 db저장.데이터베이스 종료 완료")

        ////////////////////////////////////////알림반복구현.

        val alarmManager = context.getSystemService(AppCompatActivity.ALARM_SERVICE) as AlarmManager
        println("알람매니저 접근완료")
        //람다식에 접근을 못함.(routineList가 비어있는 버그)
        RoutineList.forEach { Routine ->
            println("루틴리스트 람다식 접근완료. 현재 루틴의 개수만큼 이 출력문이 출력되어야 함")
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
            println("intent에 값 지정 완료.")
            //val repeatInterval = AlarmManager.INTERVAL_DAY//반복시간설정

            //println("캘린더ㅡ를 이용한 밀리초"+convertTime)
            if(System.currentTimeMillis()>convertTime){
                convertTime+=interval
            }
            val pendingIntent = PendingIntent.getBroadcast(
                context,
                requestCode.toInt(),
                intent,
                PendingIntent.FLAG_MUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
            )
            println("정각이 되어 pendingintent 반복 전송 완료")
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,convertTime,pendingIntent)





        //////////////////////////////////////////
    }
}
}