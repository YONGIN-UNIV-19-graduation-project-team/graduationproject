package com.nyj.routinemaker

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room
import kotlinx.android.synthetic.main.activity_main.*
import java.text.SimpleDateFormat
import java.util.*


class MainActivity : AppCompatActivity() {
    var RoutineList = arrayListOf<Routine>(
        Routine(0L,"더미","3","10",
            true,true,true,true,true,true,true,false)
    )
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        println("메인액티비티가 실행되었다.")


        var accessByPlan = intent.getBooleanExtra("access_plan",false)
        if(!accessByPlan) setFrag(0)
        else setFrag(1)

        frg1_button.setOnClickListener{
            setFrag(0)
        }
        frg2_button.setOnClickListener{
            setFrag(1)
        }

        setOnClickListener()
        resetChkbox(this)
        setAlarm(this)
    }



    private fun setFrag(fragNum : Int) {
        //fragnum이 0일때 fragment1로 , 1때 fragment2로
       val ft = supportFragmentManager.beginTransaction()
        when (fragNum){
            0 -> {
                ft.replace(R.id.main_frame, Fragment1()).commit()
                frg1_button.setTextColor(getResources().getColor(R.color.blue, getResources().newTheme()))
                frg2_button.setTextColor(Color.GRAY)
                cal_button.setVisibility(View.VISIBLE)
            }
            1 -> {
                ft.replace(R.id.main_frame, Fragment2()).commit()
                frg1_button.setTextColor(Color.GRAY)
                frg2_button.setTextColor(getResources().getColor(R.color.blue, getResources().newTheme()))
                cal_button.setVisibility(View.GONE)
            }
        }
    }

    fun setOnClickListener() {
        cal_button.setOnClickListener(View.OnClickListener {
            val intent = Intent(this, Challenge_Activity::class.java)
            startActivity(intent)
        })
    }

    fun resetChkbox(context: Context){//정각시 리스트뷰의 체크박스(db) 초기화 기능구현
        val resetAlarmManager = this.getSystemService(ALARM_SERVICE) as AlarmManager
        val resetIntent = Intent(this,ResetCheckboxReceiver::class.java)
        val resetSender = PendingIntent.getBroadcast(this,0,resetIntent,PendingIntent.FLAG_IMMUTABLE)
        val resetCal = Calendar.getInstance()
        resetCal.setTimeInMillis(System.currentTimeMillis())
        //자정 시간
        resetCal.set(Calendar.HOUR_OF_DAY,0)
        resetCal.set(Calendar.MINUTE,0)
        resetCal.set(Calendar.SECOND,0)
        resetAlarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP,
            resetCal.getTimeInMillis()+AlarmManager.INTERVAL_DAY,
            AlarmManager.INTERVAL_DAY,
            resetSender
        )
        val format = SimpleDateFormat("MM/dd kk:mm:ss")
        val setResetTime = format.format(Date(resetCal.getTimeInMillis()+AlarmManager.INTERVAL_DAY))
        Log.d("resetAlarm","ResetHour: "+setResetTime+"이 정각알람 울리는 시간")
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun setAlarm(context: Context){
        val db = Room.databaseBuilder(
            this,AppDatabase::class.java,"routine_database"
        ).allowMainThreadQueries().build()
        RoutineList = db.routine_DAO().getAll().toTypedArray().toCollection(ArrayList<Routine>())
        db.close()
        val alarmManager = this.getSystemService(ALARM_SERVICE) as AlarmManager
        RoutineList.forEach { Routine ->
            var weekList = arrayListOf<Boolean>(Routine.mon,Routine.tue,Routine.wed,Routine.thu,Routine.fri,Routine.sat,Routine.sun)

            val requestCode = Routine.id

            val triggerTime :Calendar= Calendar.getInstance()
            triggerTime.timeInMillis = System.currentTimeMillis()
            triggerTime.set(Calendar.HOUR_OF_DAY,Routine.hour.toInt())
            println("이름이"+Routine.name+"인 pendingIntent 준비 완료")
            triggerTime.set(Calendar.MINUTE,Routine.min.toInt())
            triggerTime.set(Calendar.SECOND,0)
            var convertTime = triggerTime.timeInMillis
            var interval:Long = 1000*60*60*24
            val intent = Intent(this,AlarmReceiver::class.java)

            intent.putExtra("title",(Routine.name))
            intent.putExtra("time",(convertTime.toString()))
            intent.putExtra("requestCode",requestCode.toString())
            intent.putExtra("weekList",weekList)

            //val repeatInterval = AlarmManager.INTERVAL_DAY//반복시간설정

            //println("캘린더ㅡ를 이용한 밀리초"+convertTime)
            if(System.currentTimeMillis()>convertTime){
                convertTime+=interval
            }
            val pendingIntent = PendingIntent.getBroadcast(
                this,
                requestCode.toInt(),
                intent,
                PendingIntent.FLAG_MUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
            )
            println("pendingintent 전송 완료")
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,convertTime,pendingIntent)
            //alarmManager.setRepeating(AlarmManager.RTC,convertTime,repeatInterval,pendingIntent)
        }
    }



}





