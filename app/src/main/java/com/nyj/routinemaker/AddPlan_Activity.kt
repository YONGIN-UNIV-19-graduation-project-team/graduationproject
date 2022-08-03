package com.nyj.routinemaker

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_addplan.*
import java.text.SimpleDateFormat
import java.util.*
//
class AddPlan_Activity : AppCompatActivity() {
    val formatDate = SimpleDateFormat("yyyy년 MM월 dd일", Locale.KOREA)
    val formatTime = SimpleDateFormat("HH:MM", Locale.KOREA)
    var plan_Year=""
    var plan_Month=""
    var plan_day=""
    var plan_Hour = ""
    var plan_Min=""
    var plan_Name=""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_addplan)
        //화면 나타났을때 year,month,day값 받아와야함
        //아직미구현//


        var intent = getIntent()

        var year = intent.getStringExtra("year").toString()
        var month = intent.getStringExtra("month").toString()
        var day = intent.getStringExtra("day").toString()
        //toast로 데이터 넘기기 테스트
        //Toast.makeText(this, "year : "+year, Toast.LENGTH_SHORT ).show()

        if(year != null && year.length > 0 && !year.contains("null") &&
            month != null && month.length > 0 && !month.contains("null") &&
            day != null && day.length > 0 && !day.contains("null")){
            textview_get_date.setText(year+"년"+month+"월"+day+"일")
        }
        else {
            textview_get_date.setText("error")
        }


        //날짜 선택 란
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

            },getDate.get(Calendar.YEAR),getDate.get(Calendar.MONTH),getDate.get(Calendar.DAY_OF_MONTH))
            datePicker.show()

            plan_Year=Calendar.YEAR.toString()
            plan_Month=Calendar.MONTH.toString()
            plan_day=Calendar.DAY_OF_MONTH.toString()
        })

        //시간 선택 란
        btn_pick_time.setOnClickListener(View.OnClickListener {
            val getDate = Calendar.getInstance()
            val datePicker = TimePickerDialog(this,android.R.style.Theme_Holo_Light_Dialog_MinWidth, TimePickerDialog.OnTimeSetListener { timePicker, i, i2->
                val selectDate = Calendar.getInstance()
                selectDate.set(Calendar.HOUR,i)
                selectDate.set(Calendar.MINUTE,i2)//
                val date = formatTime.format(selectDate.time)
                Toast.makeText(this, "Time : "+date, Toast.LENGTH_SHORT ).show()
                textview_get_time.text = date

            },getDate.get(Calendar.HOUR),getDate.get(Calendar.MINUTE),true)
            datePicker.show()
            plan_Hour=Calendar.HOUR.toString()
            plan_Min=Calendar.MINUTE.toString()
        })

        //저장 버튼 구현
        button3.setOnClickListener{
            //여기에 파일 입력
            plan_Name=planName.text.toString()//제목가져오기
            Toast.makeText(this
                , plan_Year+"년"+plan_Month+"월"+plan_day+"일 "+plan_Hour+"시"+plan_Min+"분"
                , Toast.LENGTH_SHORT ).show()

            val intent = Intent(this,MainActivity::class.java)
            //startActivity(intent)
        }
    }
}
