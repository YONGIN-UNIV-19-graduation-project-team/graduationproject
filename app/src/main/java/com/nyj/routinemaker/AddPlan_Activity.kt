package com.nyj.routinemaker

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Build.VERSION_CODES.M
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.room.Room
import kotlinx.android.synthetic.main.activity_addplan.*
import java.io.FileWriter
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
//..
class AddPlan_Activity : AppCompatActivity() {
    val formatDate = SimpleDateFormat("yyyy년 MM월 dd일", Locale.KOREA)

    var plan_Year=""
    var plan_Month=""
    var plan_Day=""
    var plan_Hour = ""
    var plan_Min=""
    var plan_Name=""

    var timeisselected=false
    var nameisnotnull=false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_addplan)

        //db연결
        val db = Room.databaseBuilder(
            applicationContext,AppDatabase::class.java,"databases"
        ).allowMainThreadQueries().build()



        var intent = getIntent()

        var year = intent.getStringExtra("year").toString()
        var month = ((intent.getStringExtra("month"))!!.toInt()+1).toString()//자꾸 1달 낮게 데이터가 넘겨짐.
        var day = intent.getStringExtra("day").toString()

        // ex)8월 달력 클릭시 7월로 데이터가 이전되어서 형변환후 1추가시킨후 다시형변환했음

        plan_Year=year
        plan_Month=month
        plan_Day=day


        if(year != null && year.length > 0 && !year.contains("null") &&
            month != null && month.length > 0 && !month.contains("null") &&
            day != null && day.length > 0 && !day.contains("null")){
            textview_get_date.setText(year+"년"+month+"월"+day+"일")
        }
        else {
            textview_get_date.setText("error")
        }


        //날짜 선택 란 //정상
        btn_pick_date.setOnClickListener(View.OnClickListener {
            val getDate = Calendar.getInstance()
            val datePicker = DatePickerDialog(this,android.R.style.Theme_Holo_Light_Dialog_MinWidth,DatePickerDialog.OnDateSetListener { datePicker, i, i2, i3 ->

                val selectDate = Calendar.getInstance()
                selectDate.set(Calendar.YEAR,i)
                selectDate.set(Calendar.MONTH,i2)
                selectDate.set(Calendar.DAY_OF_MONTH,i3)
                val date = formatDate.format(selectDate.time)
                Toast.makeText(this, "Date : "+date, Toast.LENGTH_SHORT ).show()
                textview_get_date.text = date

                plan_Year=selectDate.get(Calendar.YEAR).toString()
                plan_Month=(selectDate.get(Calendar.MONTH).toInt()+1).toString()//1달 낮게 값 저장되는 현상..
                plan_Day=selectDate.get(Calendar.DAY_OF_MONTH).toString()

            },getDate.get(Calendar.YEAR),getDate.get(Calendar.MONTH),getDate.get(Calendar.DAY_OF_MONTH))
            datePicker.show()


        })

        //시간 선택 란 //정상
        btn_pick_time.setOnClickListener(View.OnClickListener {
            val getDate = Calendar.getInstance()
            val datePicker = TimePickerDialog(this,android.R.style.Theme_Holo_Light_Dialog_MinWidth, TimePickerDialog.OnTimeSetListener { timePicker, i, i2->
                val selectDate = Calendar.getInstance()
                selectDate.set(Calendar.HOUR,i)
                selectDate.set(Calendar.MINUTE,i2)
                Toast.makeText(this, "Time : $i : $i2", Toast.LENGTH_SHORT ).show()
                timeisselected=true
                textview_get_time.text = "$i:$i2"
                plan_Hour=selectDate.get(Calendar.HOUR).toString()
                plan_Min=selectDate.get(Calendar.MINUTE).toString()

            },getDate.get(Calendar.HOUR),getDate.get(Calendar.MINUTE),true)
            datePicker.show()
        })


        //저장 버튼 구현.
        button3.setOnClickListener{

            plan_Name=planName.text.toString()//제목가져오기
            nameisnotnull=false
            if(plan_Name!=""){nameisnotnull=true}
            //데이터 입력
            val plan = Plan(0L,plan_Name,plan_Year,plan_Month,plan_Day,plan_Hour,plan_Min)
            if(timeisselected&&nameisnotnull) {
                db?.plan_DAO()?.insertAll(plan)
                db.close()
                val intent = Intent(this,MainActivity::class.java)
                startActivity(intent)
            }else Toast.makeText(this, "이름이나 시간이 정해지지 않았습니다.", Toast.LENGTH_SHORT).show()

        }
    }
}
