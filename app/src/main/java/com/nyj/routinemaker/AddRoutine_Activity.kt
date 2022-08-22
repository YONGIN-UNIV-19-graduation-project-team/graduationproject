package com.nyj.routinemaker

import android.content.Context
import android.content.Intent
import android.graphics.Rect
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.room.Room
import kotlinx.android.synthetic.main.activity_addroutine.*
import kotlinx.android.synthetic.main.activity_main.*
import java.io.DataOutputStream
import java.io.File
import java.io.FileOutputStream
import java.util.*

class AddRoutine_Activity : AppCompatActivity() , TimePicker.OnTimeChangedListener{

    var changed_hour=0
    var changed_minute=0
    var mon = false
    var tue = false
    var wed = false
    var thu = false
    var fri = false
    var sat = false
    var sun = false

    var timeisselected=false
    var nameisnotnull=false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_addroutine)

        val timePicker = findViewById<TimePicker>(R.id.timePicker)
        timePicker.setOnTimeChangedListener(this)
        //이전 버튼 클릭시 이벤트
        can_button.setOnClickListener {
            val intent = Intent(this,MainActivity::class.java)
            intent.putExtra("access_by_fragment",1)
            startActivity(intent)
        }
        //추가 버튼 클릭시 이벤트
        addrt_button.setOnClickListener{


            val intent = Intent(this,MainActivity::class.java)

            val name = routine_Name.text.toString()
            val hour = changed_hour.toString()
            val min = changed_minute.toString()

            if(name!="")nameisnotnull=true
            if(checkBox1.isChecked)mon=true
            if(checkBox2.isChecked)tue=true
            if(checkBox3.isChecked)wed=true
            if(checkBox4.isChecked)thu=true
            if(checkBox5.isChecked)fri=true
            if(checkBox6.isChecked)sat=true
            if(checkBox7.isChecked)sun=true

            val routine = Routine( 0L,name, hour, min, mon, tue, wed, thu, fri, sat, sun,false)

            if(!routine.mon&&!routine.tue&&!routine.wed&&!routine.thu&&!routine.fri&&!routine.sat&&!routine.sun){
                Toast.makeText(this, "요일을 한개라도 선택하세요.", Toast.LENGTH_SHORT).show()
            }
            else{
                if(timeisselected&&nameisnotnull){
                    val db = Room.databaseBuilder(
                        this.applicationContext,AppDatabase::class.java,"routine_databases"
                    ).allowMainThreadQueries().build()
                    db.routine_DAO().insertAll(routine)
                    db.close()
                    intent.putExtra("access_by_fragment",1)
                    startActivity(intent)
                }
                else{
                    Toast.makeText(this, "루틴 이름이 공백이거나 시간을 선택하지 않았습니다!", Toast.LENGTH_SHORT).show()

                }
            }
        }
    }

    override fun onTimeChanged(view: TimePicker?, hourOfDay: Int, minute: Int) {
        timeisselected=true
        val textView = findViewById<TextView>(R.id.timetext)
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