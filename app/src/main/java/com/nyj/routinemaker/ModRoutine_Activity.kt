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
import androidx.appcompat.app.AppCompatDelegate
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

    //저장할 이름,시,분 변수 초기화
    var routine_name=""
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
    var nameisnotnull=false
    var namenotcontainblank= false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        setContentView(R.layout.activity_modroutine)

        //db연결
        val db = Room.databaseBuilder(
            applicationContext,AppDatabase::class.java,"routine_databases"
        ).allowMainThreadQueries().build()

        //ListView 클릭시 id를 받아오기 위한 intent
        var intent = getIntent()
        var routineid = intent.getLongExtra("routineid",0)

        //받아온 id로 루틴 가져오기
        var get_routine = db.routine_DAO().getRoutinebyId(routineid)

        //가져온 루틴 객체의 이름,시,분 변수에 저장
        routine_name = get_routine.name
        changed_hour = get_routine.hour.toInt()
        changed_minute = get_routine.min.toInt()

        //루틴명 텍스트 띄우기
        routine_Name.setText(routine_name)

        val textView = findViewById<TextView>(R.id.timetext)

        //00시 00분으로 나타내고 싶어서 짠 코드
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

        //받아온 요일 체크박스 상태, 색 변화
        if(get_routine.mon){
            checkBox1.setChecked(true)
            checkBox1.setTextColor(Color.RED)
        }
        if(get_routine.tue){
            checkBox2.setChecked(true)
            checkBox2.setTextColor(Color.RED)
        }
        if(get_routine.wed){
            checkBox3.setChecked(true)
            checkBox3.setTextColor(Color.RED)
        }
        if(get_routine.thu){
            checkBox4.setChecked(true)
            checkBox4.setTextColor(Color.RED)
        }
        if(get_routine.fri){
            checkBox5.setChecked(true)
            checkBox5.setTextColor(Color.RED)
        }
        if(get_routine.sat){
            checkBox6.setChecked(true)
            checkBox6.setTextColor(Color.RED)
        }
        if(get_routine.sun){
            checkBox7.setChecked(true)
            checkBox7.setTextColor(Color.RED)
        }


        //Timepicker 객체 생성
        val timePicker = findViewById<TimePicker>(R.id.timePicker)

        //Timepicker 다이얼로그 생성 시 시,분 초기값 설정
        timePicker.hour = changed_hour
        timePicker.minute = changed_minute

        //리스너
        timePicker.setOnTimeChangedListener(this)

        //수정버튼 구현
        mod_button.setOnClickListener{
            val intent = Intent(this,MainActivity::class.java)

            val name = routine_Name.text.toString()
            val hour = changed_hour.toString()
            val min = changed_minute.toString()

            //이름 예외처리
            if(name!="")nameisnotnull=true else nameisnotnull=false
            if(!(name.contains(" "))) namenotcontainblank=true

            //체크박스 변수에 대입
            if(checkBox1.isChecked)mon=true
            if(checkBox2.isChecked)tue=true
            if(checkBox3.isChecked)wed=true
            if(checkBox4.isChecked)thu=true
            if(checkBox5.isChecked)fri=true
            if(checkBox6.isChecked)sat=true
            if(checkBox7.isChecked)sun=true

            //설정한 변수들로 루틴 객체 생성. id는 받아온 값
            val routine = Routine( routineid,name, hour, min, mon, tue, wed, thu, fri, sat, sun,false)

            //요일 예외처리 구현부
            if(!routine.mon&&!routine.tue&&!routine.wed&&!routine.thu&&!routine.fri&&!routine.sat&&!routine.sun){
                Toast.makeText(this, "요일을 한개라도 선택하세요.", Toast.LENGTH_SHORT).show()
            }
            else{
                //이름 예외처리 구현부, 수정부분 구현
                if(nameisnotnull&&namenotcontainblank){
                    db.routine_DAO().update(routine)
                    db.close()
                    intent.putExtra("access_by_fragment",1)
                    startActivity(intent)
                }
                else if((nameisnotnull==false)&&namenotcontainblank){
                    Toast.makeText(this, "루틴 이름이 공백입니다!", Toast.LENGTH_SHORT).show()

                }
                else if(nameisnotnull&&(namenotcontainblank==false)){
                    Toast.makeText(this, "루틴 이름에 띄어쓰기가 포함됩니다!", Toast.LENGTH_SHORT).show()

                }
            }
        }

        //삭제버튼 구현. 삭제 다이얼로그 띄우기
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

    //TimePicker 시간,분 변화 시 실시간으로 변수에 저장
    override fun onTimeChanged(p0: TimePicker?, hourOfDay: Int, minute: Int) {

        val textView = findViewById<TextView>(R.id.timetext)
        //마찬가지로 00시 00분 포맷
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