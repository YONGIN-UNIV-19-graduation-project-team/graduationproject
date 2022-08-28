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

    //루틴객체생성
    var RoutineList = arrayListOf<Routine>(
        Routine(0L,"더미","3","10",
            true,true,true,true,true,true,true,false)
    )

    //정각이 되면 받는 리시브
    override fun onReceive(context: Context, intent: Intent) {

        println("정각이 됐다!!!!!!!!!!!!!!!!!!!!1")
        //db연동
        val db = Room.databaseBuilder(
            context.applicationContext,AppDatabase::class.java,"routine_databases"
        ).allowMainThreadQueries().build()

        //체크박스를 모두 해제하는 쿼리문 호출
        db.routine_DAO().resetCheckBox()
        println("체크박스 리셋 시도 완료")

        //전체 루틴 가져오기
        RoutineList = db.routine_DAO().getAll().toTypedArray().toCollection(ArrayList<Routine>())

        //db종료
        db.close()
        println(RoutineList)
        println("루틴리스트에 db저장.데이터베이스 종료 완료")


        //알림반복구현. 알림 반복 매커니즘
        //  1. Fragment1(루틴) view가 띄워질때 db에 접근해 일괄적으로 루틴에 맞는 시간에 정확하게 pendingIntent 전송
        //  2. pendingIntent의 트리거타임이 되면 요일이 맞는지 체크, 삭제된 루틴인지 체크, 통과시 알림 울림(일회성)
        //  3. 정각이 되면(매일 반복, 오차 10분내외) db의 루틴에 접근해서 다시 루틴마다 pendingIntent 전송함
        // 이렇게 구현한 이유는 pendingIntent의 트리거타임이 정확하게 전송되려면 setExactAndAllowWhileIdle 메소드를
        // 사용해야 하는데 이는 반복적으로 알림을 보낼 수 없음. 반복적으로 알림을 보내는 메소드들은 하나같이 다 트리거타임이 정확하지 않음.(오차가 심함)
        val alarmManager = context.getSystemService(AppCompatActivity.ALARM_SERVICE) as AlarmManager
        println("알람매니저 접근완료")

        //이 부분은 AlarmReceiver.kt의 설명과 동일
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

    }
}
}