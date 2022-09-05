package com.nyj.routinemaker

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.graphics.Paint
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.room.Room
import kotlinx.android.synthetic.main.activity_dday.*
import kotlinx.android.synthetic.main.activity_main.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.ceil


class MainActivity : AppCompatActivity() {

    //전체 루틴 더미 객체리스트 생성
    var RoutineList = arrayListOf<Routine>(
        Routine(0L,"더미","3","10",
            true,true,true,true,true,true,true,false)
    )

    //년,월,일 변수 생성 더미값 설정
    var year = "2022"
    var month = "8"
    var day = "10"
    var name = "error"
    var ddayIsUsed=false
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        setContentView(R.layout.activity_main)



        val prefbyrec = this.getSharedPreferences("rec",0)

        var getID_String = prefbyrec.getLong("routineId",999).toString()

        val db = Room.databaseBuilder(
            this,AppDatabase::class.java,"routine_databases"
        ).allowMainThreadQueries().build()
        var getID_Long = getID_String.toLong()
        var rtn = db.routine_DAO().getRoutinebyId(getID_Long)
        db.close()

        if(getID_String!="999"){
            //다이얼로그 띄우기
            AlertDialog.Builder(this)
                .setMessage("최근 실패한 ["+rtn.name+"]루틴을 다시 수행하시겠습니까?")
                .setPositiveButton("예",object : DialogInterface.OnClickListener{
                    override fun onClick(dialog: DialogInterface?, which: Int) {
                        val intent = Intent(applicationContext,Test::class.java)
                        intent.putExtra("id",getID_String)
                        startActivity(intent)
                    }
                })
                .setNegativeButton("아니오",object : DialogInterface.OnClickListener{
                    override fun onClick(dialog: DialogInterface?, which: Int) {
                        var editor2 = prefbyrec.edit()
                        editor2.clear()
                        editor2.putLong("routineId",999)
                        editor2.apply()
                        Toast.makeText(applicationContext,"취소하였습니다.", Toast.LENGTH_SHORT).show()
                    }
                })
                .create()
                .show()


        }


        //무슨 프래그먼트에서 접근했는지 설정. 기본값은 챌린지 프래그먼트
        var accessByFragment = intent.getIntExtra("access_by_fragment",0)

        //intent로 받아온 값에 따라 프래그먼트 설정
        if(accessByFragment==0) setFrag(0)
        else if(accessByFragment==1)setFrag(1)
        else if(accessByFragment==2)setFrag(2)
        //기본값
        else setFrag(0)

        //화면 상단 프래그먼트 버튼에 따른 클릭 리스너
        frg0_button.setOnClickListener{
            setFrag(0)
        }
        frg1_button.setOnClickListener{
            setFrag(1)
        }
        frg2_button.setOnClickListener{
            setFrag(2)
        }

        //디데이 클릭 리스너
        d_day.setOnClickListener{
            val intent_go_to_d_day = Intent(this,Dday_Activity::class.java)
            startActivity(intent_go_to_d_day)
        }
        dday_name.setOnClickListener{
            val intent_go_to_d_day = Intent(this,Dday_Activity::class.java)
            startActivity(intent_go_to_d_day)
        }

        //디데이로부터 값 받아오기 loadData()
        loadData()
        //캘린더 객체 생성
        val settime :Calendar= Calendar.getInstance()

        //디데이 년,월,일 스트링값을 int값으로 형변환(여기에서 에러 나면 디바이스에서 앱 삭제 후 재시도해야함)
        val int_year = year.toInt()
        val int_month = (month.toInt())-1
        val int_day = day.toInt()

        //캘린더 객체에 받아온 년,월,일 값 대입
        settime.set(int_year,int_month,int_day)

        //대입한 캘린더 객체의 시간을 밀리초로 변환
        val setTime_milli = settime.timeInMillis
        //현재시간을 밀리초로 변환
        val nowtime_milli = System.currentTimeMillis()

        //디데이 계산
        val dday = (setTime_milli.toDouble()-nowtime_milli.toDouble())

        //밀리초를 일 단위로 계산하고 double형이니까 올림 했음
        var d_day_result = ceil(dday/86400000).toInt()

        //텍스트 띄우기
        if(ddayIsUsed){
            dday_name.text = name
            d_day.text = "D-"+d_day_result
        }
        else{
            dday_name.text="D-day를 설정하세요"
            d_day.text = "D-X"
            //d_day.setTextColor(Color.WHITE)
        }

        //자정이 되면 루틴들의 체크박스(수행했는지안했는지 확인하는 체크박스) 초기화하는 pendingintent 전송
        resetChkbox(this)

        //루틴들의 알람 pendingintent
        setAlarm(this)

    }


    //프래그먼트 세팅
    private fun setFrag(fragNum : Int) {

       val ft = supportFragmentManager.beginTransaction()
        when (fragNum){
            0 -> {
                ft.replace(R.id.main_frame, Fragment0()).commit()//프래그먼트이동
                frg0_button.paintFlags = Paint.UNDERLINE_TEXT_FLAG//프래그먼트밑줄
                frg0_button.setTextColor(getResources().getColor(R.color.red, getResources().newTheme()))//프래그먼트색깔
                frg2_button.setTextColor(Color.GRAY)
                frg1_button.setTextColor(Color.GRAY)
                frg1_button.paintFlags = 0//밑줄해제
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

    //정각시 루틴의 체크박스(db) 초기화 기능구현
    fun resetChkbox(context: Context){
        //알람매니저 객체생성
        val resetAlarmManager = this.getSystemService(ALARM_SERVICE) as AlarmManager
        //해당하는 리시버로 intent설정
        val resetIntent = Intent(this,ResetCheckboxReceiver::class.java)
        //Sender
        val resetSender = PendingIntent.getBroadcast(this,0,resetIntent,PendingIntent.FLAG_IMMUTABLE)
        //정각설정
        val resetCal = Calendar.getInstance()
        resetCal.setTimeInMillis(System.currentTimeMillis())
        //자정 시간
        resetCal.set(Calendar.HOUR_OF_DAY,0)
        resetCal.set(Calendar.MINUTE,0)
        resetCal.set(Calendar.SECOND,0)
        //setRepeating->정확하진 않지만 반복해줌
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

    //루틴들 전부 알람설정(pendingIntent)
    @RequiresApi(Build.VERSION_CODES.O)
    fun setAlarm(context: Context){

        //db연결
        val db = Room.databaseBuilder(
            this,AppDatabase::class.java,"routine_databases"
        ).allowMainThreadQueries().build()

        //루틴리스트 받아오기
        RoutineList = db.routine_DAO().getAll().toTypedArray().toCollection(ArrayList<Routine>())

        //db닫기
        db.close()
        val alarmManager = this.getSystemService(ALARM_SERVICE) as AlarmManager
        RoutineList.forEach { Routine ->
            //루틴의 요일리스트 선언
            var weekList = arrayListOf<Boolean>(Routine.mon,Routine.tue,Routine.wed,Routine.thu,Routine.fri,Routine.sat,Routine.sun)

            //pendingintent는 requestCode로 식별이 가능하다
            val requestCode = Routine.id

            //pendingIntent하는 시간 - triggerTime
            val triggerTime :Calendar= Calendar.getInstance()//객체 생성
            triggerTime.timeInMillis = System.currentTimeMillis()//현재시간 밀리초로 변환
            triggerTime.set(Calendar.HOUR_OF_DAY,Routine.hour.toInt())//시간 설정
            triggerTime.set(Calendar.MINUTE,Routine.min.toInt())//분 설정
            triggerTime.set(Calendar.SECOND,0)//초 설정
            var convertTime = triggerTime.timeInMillis//밀리초 객체 만듦
            var interval:Long = 1000*60*60*24//하루를 밀리초로 바꿨음
            val intent = Intent(this,AlarmReceiver::class.java)

            intent.putExtra("title",(Routine.name))
            intent.putExtra("time",(convertTime.toString()))
            intent.putExtra("requestCode",requestCode.toString())
            intent.putExtra("weekList",weekList)

            //현재 시간이 설정한 triggerTime을 지났다면 하루 추가
            if(System.currentTimeMillis()>convertTime){
                convertTime+=interval
            }
            //알람 설정하기
            val pendingIntent = PendingIntent.getBroadcast(
                this,
                requestCode.toInt(),
                intent,
                PendingIntent.FLAG_MUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
            )
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,convertTime,pendingIntent)
        }
    }

    //디데이 액티비티에서 저장한 값들을 불러오는 함수
    private fun loadData(){
        val pref = this.getSharedPreferences("a",0)
        year = pref.getString("key_year","2022").toString()
        month = pref.getString("key_month","8").toString()
        day = pref.getString("key_day","28").toString()
        name = pref.getString("key_name","디데이를 설정하세요").toString()
        ddayIsUsed = pref.getBoolean("key_used",false)
    }
}





