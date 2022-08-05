package com.nyj.routinemaker

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.TimePicker
import android.widget.Toast
import androidx.room.Room
import kotlinx.android.synthetic.main.activity_addroutine.*
import java.io.DataOutputStream
import java.io.File
import java.io.FileOutputStream
import java.util.*

class AddRoutine_Activity : AppCompatActivity() , TimePicker.OnTimeChangedListener{
    //val timePicker = findViewById<TimePicker>(R.id.timePicker)
    var routine_name=""
    var changed_hour=0
    var changed_minute=0
    var mon = false
    var tue = false
    var wed = false
    var thu = false
    var fri = false
    var sat = false
    var sun = false



    override fun onCreate(savedInstanceState: Bundle?) {//
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_addroutine)


        //db연결
        val db = Room.databaseBuilder(
            applicationContext,AppDatabase::class.java,"database"
        ).allowMainThreadQueries().build()


        val timePicker = findViewById<TimePicker>(R.id.timePicker)
        timePicker.setOnTimeChangedListener(this)

        //추가 버튼 클릭시 이벤트
        add_button.setOnClickListener{




            // MainActivity로 이동하기 위한 intent.

            val intent = Intent(this,MainActivity::class.java)

            val name = routine_Name.text.toString()
            val hour = changed_hour.toString()
            val min = changed_minute.toString()

            if(checkBox1.isChecked)mon=true
            if(checkBox2.isChecked)tue=true
            if(checkBox3.isChecked)wed=true
            if(checkBox4.isChecked)thu=true
            if(checkBox5.isChecked)fri=true
            if(checkBox6.isChecked)sat=true
            if(checkBox7.isChecked)sun=true

            val routine = Routine( name, hour, min, mon, tue, wed, thu, fri, sat, sun)
            db?.routine_DAO()?.insertAll(routine)


            startActivity(intent)

        }

    }

    override fun onTimeChanged(view: TimePicker?, hourOfDay: Int, minute: Int) {
        val textView = findViewById<TextView>(R.id.timetext)
        textView.text = "현재 설정된 시간 : $hourOfDay : $minute"
        changed_hour=hourOfDay
        changed_minute=minute
    }

}