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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_modroutine)

        //db연결
        val db = Room.databaseBuilder(
            applicationContext,AppDatabase::class.java,"database"
        ).allowMainThreadQueries().build()

        var intent = getIntent()
        var routineid = intent.getLongExtra("routineid",0)
        Toast.makeText(this, routineid.toString(), Toast.LENGTH_SHORT).show()
        var get_routine = db.routine_DAO().getRoutinebyId(routineid)
        routine_name = get_routine.name
        routine_Name.setText(routine_name)

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
        timePicker.setOnTimeChangedListener(this)

        //수정버튼 구현
        mod_button.setOnClickListener{
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

            val routine = Routine( routineid,name, hour, min, mon, tue, wed, thu, fri, sat, sun)
            db.routine_DAO().update(routine)

            startActivity(intent)


        }

        del_button.setOnClickListener{
            val intent = Intent(this,MainActivity::class.java)
            val routine = Routine(routineid,"","","",mon,tue,wed,thu,fri,sat,sun)
            db.routine_DAO().delete(routine)
            startActivity(intent)
        }

    }

    override fun onTimeChanged(p0: TimePicker?, hourOfDay: Int, minute: Int) {
        val textView = findViewById<TextView>(R.id.timetext)
        textView.text = "현재 설정된 시간 : $hourOfDay : $minute"
        changed_hour=hourOfDay
        changed_minute=minute
    }

}