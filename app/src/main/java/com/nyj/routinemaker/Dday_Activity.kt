package com.nyj.routinemaker

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.DatePickerDialog
import android.app.PendingIntent
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import kotlinx.android.synthetic.main.activity_dday.*
import kotlinx.android.synthetic.main.activity_main.*
import java.text.SimpleDateFormat
import java.util.*


class Dday_Activity : AppCompatActivity() {
    //출력하기위한 포맷
    val formatDate = SimpleDateFormat("yyyy년 M월 dd일", Locale.KOREA)

    lateinit var plan_Year : String
    lateinit var plan_Month : String
    lateinit var plan_Day : String

    var dateisselected=false

    @SuppressLint("CommitPrefEdits")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        setContentView(R.layout.activity_dday)


        //getSharedPreferences란 웹개발의 session같은 존재.
        val pref = this.getSharedPreferences("a",0)
        var name = pref.getString("key_name","").toString()

        //이전에 dday를 설정한 적이 있다면 그 이름을 가져오고 텍스트에 띄운다
        d_day_name.setText(name)

        //년,월,일은 새로 설정한다.
        var year = pref.getString("key_year","null").toString()
        var month = pref.getString("key_month","null").toString()
        var day = pref.getString("key_day","null").toString()

        //년,월,일 설정했을때 텍스트에 띄우기
        if(year!="null"&&month!="null"&&day!="null") {
            textview_get_date.text = year+"년 "+month+"월 "+day+"일"
            plan_Year = year
            plan_Month = month
            plan_Day = day
            dateisselected=true
        }

        //날짜를 설정하라는 텍스트뷰를 클릭했을 때 생성되는 DatePicker 다이얼로그
        textview_get_date.setOnClickListener(View.OnClickListener {
            dateisselected=true
            val getDate = Calendar.getInstance()
            val datePicker = DatePickerDialog(this,android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                DatePickerDialog.OnDateSetListener { datePicker, i, i2, i3 ->

                val selectDate = Calendar.getInstance()
                selectDate.set(Calendar.YEAR,i)
                selectDate.set(Calendar.MONTH,i2)
                selectDate.set(Calendar.DAY_OF_MONTH,i3)
                val date = formatDate.format(selectDate.time)
                Toast.makeText(this, "Date : "+date, Toast.LENGTH_SHORT ).show()
                textview_get_date.text = date

                plan_Year=selectDate.get(Calendar.YEAR).toString()
                    //안드로이드의 Calendar는 0월부터 시작
                plan_Month=(selectDate.get(Calendar.MONTH).toInt()+1).toString()
                plan_Day=selectDate.get(Calendar.DAY_OF_MONTH).toString()

            },getDate.get(Calendar.YEAR),getDate.get(Calendar.MONTH),getDate.get(Calendar.DAY_OF_MONTH))
            datePicker.show()


        })




        //디데이 설정하는 버튼
        add_dday_button.setOnClickListener{
            //디데이 이름 입력하도록
            if(d_day_name.text.toString() != ""){
                //이름만 변경하는경우

                //날짜를 선택했다면
                if(dateisselected) {
                    ///////////알람구현부///////////
                    val intentDday = Intent(this, DdayReceiver::class.java)

                    val alarmManager = this.getSystemService(ALARM_SERVICE) as AlarmManager

                    //트리거타임 설정
                    val triggerTime : Calendar = Calendar.getInstance()
                    triggerTime.timeInMillis = System.currentTimeMillis()
                    triggerTime.set(Calendar.YEAR,plan_Year.toInt())
                    triggerTime.set(Calendar.MONTH,plan_Month.toInt()-1)
                    triggerTime.set(Calendar.DAY_OF_MONTH,plan_Day.toInt()-3)
                    triggerTime.set(Calendar.HOUR_OF_DAY,0)
                    triggerTime.set(Calendar.MINUTE,0)
                    triggerTime.set(Calendar.SECOND,0)
                    var convertTime = triggerTime.timeInMillis
//                    var interval:Long = 1000*60*60*24//하루를 밀리초로 변환
//                    convertTime-=interval
                    println("하루 뺀 시간 : "+convertTime)
                    val format = SimpleDateFormat("MM/dd kk:mm:ss")
                    val setResetTime = format.format(Date(triggerTime.getTimeInMillis()+AlarmManager.INTERVAL_DAY))
                    Log.d("resetAlarm","ResetHour: "+setResetTime+"이 디데이 알람 울리는 시간")

                    val pendingIntent = PendingIntent.getBroadcast(
                        this,
                        1331,//디데이 전용 RC
                        intentDday,
                        PendingIntent.FLAG_MUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
                    )
                    alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,convertTime,pendingIntent)



                    ////////////////////////////

                    //디데이의 이름을 가져와 변수에 저장.
                    name = d_day_name.text.toString()
                    val intent = Intent(this, MainActivity::class.java)


                    //getSharedPreference에 년,월,일, 이름 값 저장
                    val editor = pref.edit()

                    //getSharedPreference의 값을 전부 초기화
                    editor.clear()
                    //후 저장
                    editor.putString("key_year", plan_Year)
                    editor.putString("key_month", plan_Month)
                    editor.putString("key_day", plan_Day)
                    editor.putString("key_name", name)
                    editor.putBoolean("key_used",true)
                    editor.apply()
                    startActivity(intent)

                }//날짜를 선택하지 않았으면 토스트메시지처리
                else Toast.makeText(this, "날짜를 선택하세요!", Toast.LENGTH_SHORT ).show()
                dateisselected=false
            } else Toast.makeText(this, "이름을 입력하세요!", Toast.LENGTH_SHORT ).show()

        }

        //취소 버튼
        can_button.setOnClickListener{
            val intent_can = Intent(this,MainActivity::class.java)
            startActivity(intent_can)
        }

        //디데이 초기화 버튼
        del_button.setOnClickListener{
            AlertDialog.Builder(this)
                .setMessage("정말로 디데이를 초기화하시겠습니까?")
                .setPositiveButton("예",object : DialogInterface.OnClickListener{
                    override fun onClick(dialog: DialogInterface?, which: Int) {
                        val editor = pref.edit()
                        editor.clear()
                        editor.putBoolean("key_used",false)
                        editor.apply()
                        val intent_del = Intent(applicationContext,MainActivity::class.java)
                        startActivity(intent_del)
                    }
                })
                .setNegativeButton("아니오",object : DialogInterface.OnClickListener{
                    override fun onClick(dialog: DialogInterface?, which: Int) {
                        Toast.makeText(applicationContext,"초기화를 취소하였습니다.",Toast.LENGTH_SHORT).show()
                    }
                })
                .create()
                .show()


        }
    }

    // 화면 클릭하여 키보드 숨기기 및 포커스 제거
    override fun dispatchTouchEvent(event: MotionEvent?): Boolean {
        if (event?.action === MotionEvent.ACTION_DOWN) {
            val v = currentFocus
            if (v is EditText) {
                val outRect = Rect()
                v.getGlobalVisibleRect(outRect)
                if (!outRect.contains(event.rawX.toInt(), event.rawY.toInt())) {
                    v.clearFocus()
                    val imm: InputMethodManager =
                        getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0)
                }
            }
        }
        return super.dispatchTouchEvent(event)
    }

}