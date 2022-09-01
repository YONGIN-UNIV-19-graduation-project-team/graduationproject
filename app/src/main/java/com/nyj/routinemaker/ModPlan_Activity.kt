package com.nyj.routinemaker

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Rect
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.room.Room
import kotlinx.android.synthetic.main.activity_addplan.*
import kotlinx.android.synthetic.main.activity_addplan.planName
import kotlinx.android.synthetic.main.activity_addplan.textview_get_date
import kotlinx.android.synthetic.main.activity_addplan.textview_get_time
import kotlinx.android.synthetic.main.activity_modplan.*
import kotlinx.android.synthetic.main.activity_modroutine.*
import kotlinx.android.synthetic.main.activity_modroutine.del_button
import kotlinx.android.synthetic.main.activity_modroutine.mod_button
import kotlinx.android.synthetic.main.list_item_plan.*
import java.text.SimpleDateFormat
import java.util.*

class ModPlan_Activity : AppCompatActivity() {

    //출력을 위한 스트링 포맷
    val formatDate = SimpleDateFormat("yyyy년 MM월 dd일", Locale.KOREA)

    //년, 월, 일, 시간, 이름 변수 초기화
    var plan_Year=""
    var plan_Month=""
    var plan_Day=""
    var plan_Hour = ""
    var plan_Min=""
    var plan_Name=""

    //예외처리를 위한 변수 생성
    var timeisselected=true
    var nameisnotnull=false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_modplan)

        //db연결
        val db = Room.databaseBuilder(
            applicationContext,AppDatabase::class.java,"routine_databases"
        ).allowMainThreadQueries().build()

        //리스트뷰를 클릭했을 때 id값을 받아오기 위한 Intent
        var intent = getIntent()
        var planid = intent.getLongExtra("planid",0)

        //여기에서 해당id의 plan 가져옴
        var get_plan = db.plan_DAO().getPlanbyId(planid)

        //받아온 년월일 띄우기
        textview_get_date.text = get_plan.year+"년 "+get_plan.month+"월 "+get_plan.day+"일"

        //받아온 시간 변수에 저장
        val gethour = get_plan.hour.toInt()
        val getmin = get_plan.min.toInt()

        //00시 00분으로 띄우고 싶어서 짠 코드
        if(get_plan.hour.toInt()<10&&get_plan.min.toInt()<10) {
            textview_get_time.text = "0$gethour : 0$getmin"
        }
        else {
            if (get_plan.min.toInt()<10) {
                textview_get_time.text = "$gethour : 0$getmin"
            }
            else
                if(get_plan.hour.toInt()<10) {
                    textview_get_time.text = "0$gethour : $getmin"
                }
                else textview_get_time.text = "$gethour : $getmin"
        }


        //이름,년월일,시간 변수에저장
        plan_Name = get_plan.name
        planName.setText(plan_Name)
        plan_Year = get_plan.year
        plan_Month=get_plan.month
        plan_Day = get_plan.day
        plan_Hour = get_plan.hour
        plan_Min = get_plan.min

        //날짜 선택 다이얼로그
        textview_get_date.setOnClickListener(View.OnClickListener {
            //캘린더 객체 생성
            val getDate = Calendar.getInstance()
            //다이얼로그 구현부
            val datePicker = DatePickerDialog(this,android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                DatePickerDialog.OnDateSetListener { datePicker, i, i2, i3 ->
                    //캘린더 객체 생성
                    val selectDate = Calendar.getInstance()
                    selectDate.set(Calendar.YEAR,i)//Datepicker의 년 캘린더객체에 저장
                    selectDate.set(Calendar.MONTH,i2)//월 저장
                    selectDate.set(Calendar.DAY_OF_MONTH,i3)//일 저장
                    val date = formatDate.format(selectDate.time)//아까 구현한 포맷에 년월일 저장
                    Toast.makeText(this, "Date : "+date, Toast.LENGTH_SHORT ).show()
                    textview_get_date.text = date

                    //다이얼로그로 선택한 년월일 새로 저장
                    plan_Year=selectDate.get(Calendar.YEAR).toString()
                    plan_Month=(selectDate.get(Calendar.MONTH).toInt()+1).toString()//1달 낮게 값 저장되는 현상..
                    plan_Day=selectDate.get(Calendar.DAY_OF_MONTH).toString()

            },getDate.get(Calendar.YEAR),getDate.get(Calendar.MONTH),getDate.get(Calendar.DAY_OF_MONTH))
            datePicker.show()


        })


        //시간 선택 다이얼로그
        textview_get_time.setOnClickListener(View.OnClickListener {
            val getDate = Calendar.getInstance()
            getDate.set(Calendar.HOUR,gethour)
            getDate.set(Calendar.MINUTE,getmin)
            val datePicker = TimePickerDialog(this,android.R.style.Theme_Holo_Light_Dialog_MinWidth, TimePickerDialog.OnTimeSetListener { timePicker, i, i2->
                val selectDate = Calendar.getInstance()

                selectDate.set(Calendar.HOUR,i)
                selectDate.set(Calendar.MINUTE,i2)
                Toast.makeText(this, "Time : $i : $i2", Toast.LENGTH_SHORT ).show()
                //예외처리를 위한 시간선택 변수
                timeisselected=true

                //00시 00분으로 나타내고싶어서 짠 코드
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

                //Timepicker로 설정한 시, 분 변수에 저장
                plan_Hour=i.toString()
                plan_Min=i2.toString()

            },getDate.get(Calendar.HOUR),getDate.get(Calendar.MINUTE),true)
            datePicker.show()
        })


        //수정버튼
        mod_button.setOnClickListener{
            val intent = Intent(this,MainActivity::class.java)
            nameisnotnull=false
            plan_Name = planName.text.toString()
            if(plan_Name!="")nameisnotnull=true

            //쿼리문 쉽게 쓰려고 년,월,일 String 합쳤음
            val add_date = plan_Year+plan_Month+plan_Day

            //id만 같은 새로운 객체 생성
            val plan = Plan(planid,plan_Name,plan_Year,plan_Month,plan_Day,plan_Hour,plan_Min,add_date)
            //예외처리 구현부
            if(timeisselected) {
                if(nameisnotnull) {
                    db.plan_DAO().update(plan)
                    db.close()
                    intent.putExtra("access_by_fragment",2)
                    startActivity(intent)
                }else Toast.makeText(this, "이름을 공백으로 사용할 수 없습니다.", Toast.LENGTH_SHORT).show()
            }else {
                timeisselected=false
                Toast.makeText(this, "시간이 선택되지 않았습니다.", Toast.LENGTH_SHORT).show()
            }
        }

        //삭제버튼
        del_button.setOnClickListener{
            //정말로 삭제할건지 묻는 다이얼로그
            AlertDialog.Builder(this)
                .setMessage("정말로 삭제하시겠습니까?")
                .setPositiveButton("예",object : DialogInterface.OnClickListener{
                    override fun onClick(dialog: DialogInterface?, which: Int) {
                        val intent = Intent(applicationContext,MainActivity::class.java)
                        val plan = Plan(planid,plan_Name,plan_Year,plan_Month,plan_Day,plan_Hour,plan_Min,"")
                        db.plan_DAO().delete(plan)
                        db.close()
                        intent.putExtra("access_by_fragment",2)
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
        //취소버튼
        cancel_modplan_button.setOnClickListener{
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