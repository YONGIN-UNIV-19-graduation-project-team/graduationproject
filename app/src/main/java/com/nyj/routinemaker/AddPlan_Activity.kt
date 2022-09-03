package com.nyj.routinemaker

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.graphics.Rect
import android.hardware.input.InputManager
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room
import kotlinx.android.synthetic.main.activity_addplan.*
import java.text.SimpleDateFormat
import java.util.*


//..
class AddPlan_Activity : AppCompatActivity() {
    //출력 형식 포맷
    val formatDate = SimpleDateFormat("yyyy년 MM월 dd일", Locale.KOREA)

    //정해지는 변수 초기화. 코틀린에서는 초기값 설정을 해줘야하므로 lateinit 변수를 사용해서 초기값 없이 변수선언.
    lateinit var plan_Year : String
    lateinit var plan_Month : String
    lateinit var plan_Day : String
    var plan_Hour = ""
    var plan_Min = ""
    var plan_Name = ""

    //할일 추가할 때 예외처리를 위한 변수 초기화
    var timeisselected=false
    var nameisnotnull=false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_addplan)

        //db연결
        val db = Room.databaseBuilder(
            applicationContext,AppDatabase::class.java,"routine_databases"
        ).allowMainThreadQueries().build()


        //Fragment2에서 캘린더 클릭시 intent로 전달한 년,월,일 값을 받아온다
        var intent = getIntent()

        var year = intent.getStringExtra("year").toString()
        var month = (intent.getStringExtra("month")).toString()
        var day = intent.getStringExtra("day").toString()

        plan_Year=year
        plan_Month=month
        plan_Day=day


        //intent 값 수신 시 예외처리 부분
        if(year != null && year.length > 0 && !year.contains("null") &&
            month != null && month.length > 0 && !month.contains("null") &&
            day != null && day.length > 0 && !day.contains("null")){
            textview_get_date.setText(year+"년"+month+"월"+day+"일")
        }
        else {
            textview_get_date.setText("error")
        }


        //날짜 선택 다이얼로그
        textview_get_date.setOnClickListener(View.OnClickListener {
            val getDate = Calendar.getInstance()
            val datePicker = DatePickerDialog(this,android.R.style.Theme_Holo_Light_Dialog_MinWidth,DatePickerDialog.OnDateSetListener { datePicker, i, i2, i3 ->

                val selectDate = Calendar.getInstance()
                selectDate.set(Calendar.YEAR,i)
                selectDate.set(Calendar.MONTH,i2)
                selectDate.set(Calendar.DAY_OF_MONTH,i3)
                val date = formatDate.format(selectDate.time)
                Toast.makeText(this, "Date : "+date, Toast.LENGTH_SHORT ).show()
                textview_get_date.text = date

                //String으로 형변환 후 변수에 저장
                plan_Year=selectDate.get(Calendar.YEAR).toString()
                //안드로이드의 Calendar 라이브러리에서 month값은 0부터 시작된다.
                plan_Month=(selectDate.get(Calendar.MONTH).toInt()+1).toString()
                plan_Day=selectDate.get(Calendar.DAY_OF_MONTH).toString()

            },getDate.get(Calendar.YEAR),getDate.get(Calendar.MONTH),getDate.get(Calendar.DAY_OF_MONTH))
            datePicker.show()
        })

        //시간 선택 다이얼로그
        textview_get_time.setOnClickListener(View.OnClickListener {
            val getDate = Calendar.getInstance()
            val datePicker = TimePickerDialog(this,android.R.style.Theme_Holo_Light_Dialog_MinWidth, TimePickerDialog.OnTimeSetListener { timePicker, i, i2->
                val selectDate = Calendar.getInstance()
                selectDate.set(Calendar.HOUR,i)
                selectDate.set(Calendar.MINUTE,i2)
                Toast.makeText(this, "Time : $i : $i2", Toast.LENGTH_SHORT ).show()
                timeisselected=true

                //00시 00분으로 나타내고 싶어서 짠 코드
                if(i<10&&i2<10) {
                    textview_get_time.text = "0$i : 0$i2"
                }
                else {
                    if (i2<10) {
                        textview_get_time.text = "$i : 0$i2"
                    }
                    else
                        if(i<10) {
                            textview_get_time.text = "0$i : $i2"
                        }
                        else textview_get_time.text = "$i : $i2"
                }
                //String으로 형변환 후 변수에 저장
                plan_Hour=i.toString()
                plan_Min=i2.toString()

            },getDate.get(Calendar.HOUR),getDate.get(Calendar.MINUTE),true)
            datePicker.show()
        })


        //저장 버튼 구현.
        addplan_button.setOnClickListener{
            //제목가져오기
            plan_Name=planName.text.toString()

            //버튼을 눌렀을 때 초기값을 다시 재설정해준다.(예외처리가 되었을때 true로 바뀌기 때문)
            nameisnotnull=false

            //할일의 이름이 공백이면(혹은 지정하지 않으면) true로 변경
            if(plan_Name!=""){nameisnotnull=true}

            //쉬운 쿼리문을 사용하고 싶어서 date String을 합쳤다
            val add_date = plan_Year+plan_Month+plan_Day

            if(timeisselected==false){
                val i=9
                val i2 =0

                //00시 00분으로 나타내고 싶어서 짠 코드
                if(i<10&&i2<10) {
                    textview_get_time.text = "0$i : 0$i2"
                }
                else {
                    if (i2<10) {
                        textview_get_time.text = "$i : 0$i2"
                    }
                    else
                        if(i<10) {
                            textview_get_time.text = "0$i : $i2"
                        }
                        else textview_get_time.text = "$i : $i2"
                }
                //String으로 형변환 후 변수에 저장
                plan_Hour=i.toString()
                plan_Min=i2.toString()
                timeisselected=true
            }


            //지금까지 입력했던 변수들로 plan객체 생성
            val plan = Plan(0L,plan_Name,plan_Year,plan_Month,plan_Day,plan_Hour,plan_Min,add_date)

            //시간 선택했는지, 이름이 공백이아닌지 예외처리
            if(timeisselected&&nameisnotnull) {
                //데이터베이스에 생성한 plan객체 insert
                db.plan_DAO().insertAll(plan)
                //db종료
                db.close()
                //메인액티비티로 이동하기 위한 intent. intent로 값도 전달하여 할일 프래그먼트로 이동하도록 했다
                val intent = Intent(this,MainActivity::class.java)
                intent.putExtra("access_by_fragment",2)
                startActivity(intent)
            }else Toast.makeText(this, "이름을 정해주세요.", Toast.LENGTH_SHORT).show()

        }
        //이전 버튼 구현
        cancel_button.setOnClickListener{
            val intent = Intent(this,MainActivity::class.java)
            intent.putExtra("access_by_fragment",2)
            db.close()
            startActivity(intent)
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
