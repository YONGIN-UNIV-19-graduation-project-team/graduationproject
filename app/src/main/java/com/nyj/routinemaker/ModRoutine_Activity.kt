package com.nyj.routinemaker

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.CheckBox
import android.widget.TextView
import android.widget.TimePicker
import android.widget.Toast
import androidx.room.Room
import kotlinx.android.synthetic.main.activity_addroutine.*
import kotlinx.android.synthetic.main.activity_modroutine.*
import kotlinx.android.synthetic.main.activity_modroutine.checkBox1
import kotlinx.android.synthetic.main.activity_modroutine.checkBox2
import kotlinx.android.synthetic.main.activity_modroutine.checkBox3
import kotlinx.android.synthetic.main.activity_modroutine.checkBox4
import kotlinx.android.synthetic.main.activity_modroutine.checkBox5
import kotlinx.android.synthetic.main.activity_modroutine.checkBox6
import kotlinx.android.synthetic.main.activity_modroutine.checkBox7
import kotlinx.android.synthetic.main.activity_modroutine.routine_Name
import java.time.MonthDay

class ModRoutine_Activity : AppCompatActivity() ,TimePicker.OnTimeChangedListener{
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

    //var timeisselected=false
    var nameisnotnull=false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_modroutine)

        //db연결
        val db = Room.databaseBuilder(
            applicationContext,AppDatabase::class.java,"routine_database"
        ).allowMainThreadQueries().build()

        var intent = getIntent()
        var routineid = intent.getLongExtra("routineid",0)

        //id확인 Toast
        //Toast.makeText(this, routineid.toString(), Toast.LENGTH_SHORT).show()
        var get_routine = db.routine_DAO().getRoutinebyId(routineid)
        routine_name = get_routine.name
        changed_hour = get_routine.hour.toInt()
        changed_minute = get_routine.min.toInt()
        routine_Name.setText(routine_name)

        val textView = findViewById<TextView>(R.id.timetext)

        if(changed_hour<10&&changed_minute<10) {
            textView.text = "0$changed_hour : 0$changed_minute"
        }
        else {
            if (changed_minute<10) {
                textView.text = "$changed_hour : 0$changed_minute"
            }
            else
                if(changed_hour<10) {
                    textView.text = "0$changed_hour : $changed_minute"
                }
                else textView.text = "$changed_hour : $changed_minute"
        }




        val checkBox01 = findViewById<CheckBox>(R.id.checkBox1)
        val checkBox02 = findViewById<CheckBox>(R.id.checkBox2)
        val checkBox03 = findViewById<CheckBox>(R.id.checkBox3)
        val checkBox04 = findViewById<CheckBox>(R.id.checkBox4)
        val checkBox05 = findViewById<CheckBox>(R.id.checkBox5)
        val checkBox06 = findViewById<CheckBox>(R.id.checkBox6)
        val checkBox07 = findViewById<CheckBox>(R.id.checkBox7)

        if(get_routine.mon)checkBox01.setChecked(true)
        if(get_routine.tue)checkBox02.setChecked(true)
        if(get_routine.wed)checkBox03.setChecked(true)
        if(get_routine.thu)checkBox04.setChecked(true)
        if(get_routine.fri)checkBox05.setChecked(true)
        if(get_routine.sat)checkBox06.setChecked(true)
        if(get_routine.sun)checkBox07.setChecked(true)



        val timePicker = findViewById<TimePicker>(R.id.timePicker)
        timePicker.hour = changed_hour
        timePicker.minute = changed_minute
        timePicker.setOnTimeChangedListener(this)

        //수정버튼 구현
        mod_button.setOnClickListener{
            val intent = Intent(this,MainActivity::class.java)

            val name = routine_Name.text.toString()
            val hour = changed_hour.toString()
            val min = changed_minute.toString()

            if(name!="")nameisnotnull=true else nameisnotnull=false
            if(checkBox1.isChecked)mon=true
            if(checkBox2.isChecked)tue=true
            if(checkBox3.isChecked)wed=true
            if(checkBox4.isChecked)thu=true
            if(checkBox5.isChecked)fri=true
            if(checkBox6.isChecked)sat=true
            if(checkBox7.isChecked)sun=true

            val routine = Routine( routineid,name, hour, min, mon, tue, wed, thu, fri, sat, sun,false)

            if(!routine.mon&&!routine.tue&&!routine.wed&&!routine.thu&&!routine.fri&&!routine.sat&&!routine.sun){
                Toast.makeText(this, "요일을 한개라도 선택하세요.", Toast.LENGTH_SHORT).show()
            }
            else{
                if(nameisnotnull){
                    db.routine_DAO().update(routine)
                    db.close()
                    startActivity(intent)
                }
                else{
                    Toast.makeText(this, "루틴 이름이 공백입니다!", Toast.LENGTH_SHORT).show()
                }
            }
        }
        //삭제버튼 구현
        del_button.setOnClickListener{
            val intent = Intent(this,MainActivity::class.java)
            val routine = Routine(routineid,"","","",mon,tue,wed,thu,fri,sat,sun,false)
            db.routine_DAO().delete(routine)
            db.close()
            startActivity(intent)
        }


    }

    override fun onTimeChanged(p0: TimePicker?, hourOfDay: Int, minute: Int) {

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

}