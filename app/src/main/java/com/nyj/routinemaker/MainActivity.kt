package com.nyj.routinemaker

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Paint
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room
import kotlinx.android.synthetic.main.activity_dday.*
import kotlinx.android.synthetic.main.activity_main.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.ceil


class MainActivity : AppCompatActivity() {
    var RoutineList = arrayListOf<Routine>(
        Routine(0L,"더미","3","10",
            true,true,true,true,true,true,true,false)
    )
    var year = "2022"
    var month = "8"
    var day = "10"
    var name = "error"

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var accessByFragment = intent.getIntExtra("access_by_fragment",0)
        if(accessByFragment==0) setFrag(0)
        else if(accessByFragment==1)setFrag(1)
        else if(accessByFragment==2)setFrag(2)
        else setFrag(0)
        frg0_button.setOnClickListener{
            setFrag(0)
        }
        frg1_button.setOnClickListener{
            setFrag(1)
        }
        frg2_button.setOnClickListener{
            setFrag(2)
        }

        d_day.setOnClickListener{
            val intent_go_to_d_day = Intent(this,Dday_Activity::class.java)
            startActivity(intent_go_to_d_day)
        }
        dday_name.setOnClickListener{
            val intent_go_to_d_day = Intent(this,Dday_Activity::class.java)
            startActivity(intent_go_to_d_day)
        }
        loadData()
        val settime :Calendar= Calendar.getInstance()
        val int_year = year?.toInt()
        val int_month = (month?.toInt())-1
        val int_day = day?.toInt()
        settime.set(int_year,int_month,int_day)
        val setTime_milli = settime.timeInMillis
        println("@@@@@@@"+setTime_milli.toString())
        val nowtime_milli = System.currentTimeMillis()
        println("@@@@@@@"+nowtime_milli.toString())
        val dday = (setTime_milli.toDouble()-nowtime_milli.toDouble())
        println(dday)
        println((dday/86400000).toString())
        var d_day_result = ceil(dday/86400000).toInt()


        dday_name.text = name
        d_day.text = "D-"+d_day_result





        resetChkbox(this)
        setAlarm(this)

    }



    private fun setFrag(fragNum : Int) {

       val ft = supportFragmentManager.beginTransaction()
        when (fragNum){
            0 -> {
                ft.replace(R.id.main_frame, Fragment0()).commit()
                frg0_button.paintFlags = Paint.UNDERLINE_TEXT_FLAG
                frg0_button.setTextColor(getResources().getColor(R.color.red, getResources().newTheme()))
                frg2_button.setTextColor(Color.GRAY)
                frg1_button.setTextColor(Color.GRAY)
                frg1_button.paintFlags = 0
                frg2_button.paintFlags = 0

            }
            1 -> {
                ft.replace(R.id.main_frame, Fragment1()).commit()
                frg1_button.paintFlags = Paint.UNDERLINE_TEXT_FLAG
                frg0_button.setTextColor(Color.GRAY)
                frg1_button.setTextColor(getResources().getColor(R.color.red, getResources().newTheme()))
                frg2_button.setTextColor(Color.GRAY)
                frg0_button.paintFlags = 0
                frg2_button.paintFlags = 0

            }
            2 -> {
                ft.replace(R.id.main_frame, Fragment2()).commit()
                frg2_button.paintFlags = Paint.UNDERLINE_TEXT_FLAG
                frg0_button.setTextColor(Color.GRAY)
                frg1_button.setTextColor(Color.GRAY)
                frg2_button.setTextColor(getResources().getColor(R.color.red, getResources().newTheme()))
                frg0_button.paintFlags = 0
                frg1_button.paintFlags = 0
            }
        }
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
            this,AppDatabase::class.java,"routine_databases"
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
        }
    }


    private fun loadData(){
        val pref = this.getSharedPreferences("a",0)
        year = pref.getString("key_year","2022").toString()
        month = pref.getString("key_month","8").toString()
        day = pref.getString("key_day","28").toString()
        name = pref.getString("key_name","디데이를 설정하세요").toString()

    }
}





