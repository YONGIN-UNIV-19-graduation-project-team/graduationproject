package com.nyj.routinemaker

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.graphics.Rect
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AlertDialog
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
            applicationContext,AppDatabase::class.java,"routine_databases"
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



        //지워도 되는 코드
        val checkBox01 = findViewById<CheckBox>(R.id.checkBox1)
        val checkBox02 = findViewById<CheckBox>(R.id.checkBox2)
        val checkBox03 = findViewById<CheckBox>(R.id.checkBox3)
        val checkBox04 = findViewById<CheckBox>(R.id.checkBox4)
        val checkBox05 = findViewById<CheckBox>(R.id.checkBox5)
        val checkBox06 = findViewById<CheckBox>(R.id.checkBox6)
        val checkBox07 = findViewById<CheckBox>(R.id.checkBox7)
        //대신 01->1로 바꿔 놓아야함


        if(get_routine.mon){
            checkBox01.setChecked(true)
            checkBox01.setTextColor(Color.RED)
        }
        if(get_routine.tue){
            checkBox02.setChecked(true)
            checkBox02.setTextColor(Color.RED)
        }
        if(get_routine.wed){
            checkBox03.setChecked(true)
            checkBox03.setTextColor(Color.RED)
        }
        if(get_routine.thu){
            checkBox04.setChecked(true)
            checkBox4.setTextColor(Color.RED)
        }
        if(get_routine.fri){
            checkBox05.setChecked(true)
            checkBox5.setTextColor(Color.RED)
        }
        if(get_routine.sat){
            checkBox06.setChecked(true)
            checkBox6.setTextColor(Color.RED)
        }
        if(get_routine.sun){
            checkBox07.setChecked(true)
            checkBox7.setTextColor(Color.RED)
        }



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
                    intent.putExtra("access_by_fragment",1)
                    startActivity(intent)
                }
                else{
                    Toast.makeText(this, "루틴 이름이 공백입니다!", Toast.LENGTH_SHORT).show()
                }
            }
        }
        //삭제버튼 구현
        del_button.setOnClickListener{
            AlertDialog.Builder(this)
                .setMessage("정말로 삭제하시겠습니까?")
                .setPositiveButton("예",object : DialogInterface.OnClickListener{
                    override fun onClick(dialog: DialogInterface?, which: Int) {
                        val intent = Intent(applicationContext,MainActivity::class.java)
                        val routine = Routine(routineid,"","","",mon,tue,wed,thu,fri,sat,sun,false)
                        db.routine_DAO().delete(routine)
                        db.close()
                        intent.putExtra("access_by_fragment",1)
                        startActivity(intent)
                    }
                })
                .setNegativeButton("아니오",object : DialogInterface.OnClickListener{
                    override fun onClick(dialog: DialogInterface?, which: Int) {
                        Toast.makeText(applicationContext,"삭제를 취소하였습니다.",Toast.LENGTH_SHORT).show()
                    }
                })
                .create()
                .show()

        }
        //취소버튼 구현
        cancel_modroutine_button.setOnClickListener{
            val intent = Intent(this,MainActivity::class.java)
            intent.putExtra("access_by_fragment",1)
            db.close()
            startActivity(intent)
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