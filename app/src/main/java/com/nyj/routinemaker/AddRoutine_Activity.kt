package com.nyj.routinemaker

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Rect
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AppCompatDelegate
import androidx.room.Room
import kotlinx.android.synthetic.main.activity_addroutine.*
import kotlinx.android.synthetic.main.activity_main.*
import java.io.DataOutputStream
import java.io.File
import java.io.FileOutputStream
import java.util.*

class AddRoutine_Activity : AppCompatActivity() , TimePicker.OnTimeChangedListener{
    //TimePicker로 변경되는 시간들을 저장할 시간,분 변수 초기화
    var changed_hour=0
    var changed_minute=0

    //요일변수 초기화
    var mon = false
    var tue = false
    var wed = false
    var thu = false
    var fri = false
    var sat = false
    var sun = false

    //예외처리를 위한 변수 초기화
    var timeisselected=false
    var nameisnotnull=false
    var namenotcontainblank= false

    @SuppressLint("ResourceAsColor")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        setContentView(R.layout.activity_addroutine)

        //TimePicker 선언
        val timePicker = findViewById<TimePicker>(R.id.timePicker)
        //시간 선택할때 실시간으로 값이 바로바로 저장되도록 선언
        timePicker.setOnTimeChangedListener(this)

        //이전 버튼 클릭시 이벤트. intent에 값을 전달해 루틴 프래그먼트로 이동함
        can_button.setOnClickListener {
            val intent = Intent(this,MainActivity::class.java)
            intent.putExtra("access_by_fragment",1)
            startActivity(intent)
        }

        //추가 버튼 클릭시 이벤트
        addrt_button.setOnClickListener{


            val intent = Intent(this,MainActivity::class.java)

            //editText에 입력한 루틴명, TimePicker로 설정한 시,분을 변수에 저장
            val name = routine_Name.text.toString()
            val hour = changed_hour.toString()
            val min = changed_minute.toString()

            //예외처리를 위한 변수. 루틴명이 공백이거나 띄어쓰기를 포함하면 true로 지정
            if(name!="")nameisnotnull=true
            if(!(name.contains(" "))) namenotcontainblank=true

            //체크박스의 상태를 변수에 각각 저장한다
            if(checkBox1.isChecked)mon=true
            if(checkBox2.isChecked)tue=true
            if(checkBox3.isChecked)wed=true
            if(checkBox4.isChecked)thu=true
            if(checkBox5.isChecked)fri=true
            if(checkBox6.isChecked)sat=true
            if(checkBox7.isChecked)sun=true

            //값을 지정한 변수들로 routine객체 생성
            val routine = Routine( 0L,name, hour, min, mon, tue, wed, thu, fri, sat, sun,false)

            //요일이 선택되지 않았을 때(체크박스를 한개도 체크하지 않았을 때)의 예외처리
            if(!routine.mon&&!routine.tue&&!routine.wed&&!routine.thu&&!routine.fri&&!routine.sat&&!routine.sun){
                Toast.makeText(this, "요일을 한개라도 선택하세요.", Toast.LENGTH_SHORT).show()
            }
            else{
                //시간을 선택하지 않았거나 이름이 공백일 때의 예외처리
                if(timeisselected&&nameisnotnull&&namenotcontainblank){
                    //정상적인 루틴추가 구현 부분
                    val db = Room.databaseBuilder(
                        this.applicationContext,AppDatabase::class.java,"routine_databases"
                    ).allowMainThreadQueries().build()
                    //생성한 Routine객체 DB에 insert
                    db.routine_DAO().insertAll(routine)
                    db.close()
                    intent.putExtra("access_by_fragment",1)
                    startActivity(intent)
                }
                else if((timeisselected==false)&&nameisnotnull&&namenotcontainblank){
                    Toast.makeText(this, "시간을 선택하지 않았습니다!", Toast.LENGTH_SHORT).show()

                }
                else if(timeisselected&&(nameisnotnull==false)&&namenotcontainblank){
                    Toast.makeText(this, "루틴 이름이 공백입니다!", Toast.LENGTH_SHORT).show()

                }
                else if(timeisselected&&nameisnotnull&&(namenotcontainblank==false)){
                    Toast.makeText(this, "루틴 이름에 띄어쓰기가 포함됩니다!", Toast.LENGTH_SHORT).show()

                }
            }
        }

        //요일 체크박스 선택및 해제시 텍스트 컬러 변경
        checkBox1.setOnClickListener {
            if(checkBox1.currentTextColor == Color.RED)
                checkBox1.setTextColor(resources.getColor(R.color.gray,resources.newTheme()))
            else
                checkBox1.setTextColor(Color.RED)
        }
        checkBox2.setOnClickListener {
            if(checkBox2.currentTextColor == Color.RED)
                checkBox2.setTextColor(resources.getColor(R.color.gray,resources.newTheme()))
            else
                checkBox2.setTextColor(Color.RED)
        }
        checkBox3.setOnClickListener {
            if(checkBox3.currentTextColor == Color.RED)
                checkBox3.setTextColor(resources.getColor(R.color.gray,resources.newTheme()))
            else
                checkBox3.setTextColor(Color.RED)
        }
        checkBox4.setOnClickListener {
            if(checkBox4.currentTextColor == Color.RED)
                checkBox4.setTextColor(resources.getColor(R.color.gray,resources.newTheme()))
            else
                checkBox4.setTextColor(Color.RED)
        }
        checkBox5.setOnClickListener {
            if(checkBox5.currentTextColor == Color.RED)
                checkBox5.setTextColor(resources.getColor(R.color.gray,resources.newTheme()))
            else
                checkBox5.setTextColor(Color.RED)
        }
        checkBox6.setOnClickListener {
            if(checkBox6.currentTextColor == Color.RED)
                checkBox6.setTextColor(resources.getColor(R.color.gray,resources.newTheme()))
            else
                checkBox6.setTextColor(Color.RED)
        }
        checkBox7.setOnClickListener {
            if(checkBox7.currentTextColor == Color.RED)
                checkBox7.setTextColor(resources.getColor(R.color.gray,resources.newTheme()))
            else
                checkBox7.setTextColor(Color.RED)
        }

    }

    //TimePicker의 시,분 설정할때의 함수 구현
    override fun onTimeChanged(view: TimePicker?, hourOfDay: Int, minute: Int) {
        //시간 선택 예외처리 변수. 한번이라도 설정했으면 true
        timeisselected=true
        //선택한 시간 textview로 띄우기
        val textView = findViewById<TextView>(R.id.timetext)

        //00시 00분으로 나타내고 싶어서 짠 코드
        if(hourOfDay<10&&minute<10) {
            textView.text = "0$hourOfDay : 0$minute"
        }
        else {
            if (minute<10) {
                textView.text = "$hourOfDay : 0$minute"
            }
            else
                if(hourOfDay<10) {
                    textView.text = "0$hourOfDay : $minute"
                }
                else textView.text = "$hourOfDay : $minute"
        }
        //값 저장
        changed_hour=hourOfDay
        changed_minute=minute
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