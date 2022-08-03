package com.nyj.routinemaker

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_addplan.*
import java.io.FileWriter
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
//
class AddPlan_Activity : AppCompatActivity() {
    val formatDate = SimpleDateFormat("yyyy년 MM월 dd일", Locale.KOREA)
    val formatTime = SimpleDateFormat("HH:MM", Locale.KOREA)
    var plan_Year=""
    var plan_Month=""
    var plan_Day=""
    var plan_Hour = ""
    var plan_Min=""
    var plan_Name=""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_addplan)



        var intent = getIntent()

        var year = intent.getStringExtra("year").toString()
        //var month = intent.getStringExtra("month").toString()
        var day = intent.getStringExtra("day").toString()
        var month = ((intent.getStringExtra("month"))!!.toInt()+1).toString()

        //toast로 데이터 넘기기 테스트
        //Toast.makeText(this, "year : "+year, Toast.LENGTH_SHORT ).show()

        plan_Year=year
        plan_Month=month
        plan_Day=day


        if(year != null && year.length > 0 && !year.contains("null") &&
            month != null && month.length > 0 && !month.contains("null") &&
            day != null && day.length > 0 && !day.contains("null")){
            textview_get_date.setText(year+"년"+(month).toString()+"월"+day+"일")
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

                plan_Year=selectDate.get(Calendar.YEAR).toString()
                plan_Month=selectDate.get(Calendar.MONTH).toString()
                plan_Day=selectDate.get(Calendar.DAY_OF_MONTH).toString()

            },getDate.get(Calendar.YEAR),getDate.get(Calendar.MONTH),getDate.get(Calendar.DAY_OF_MONTH))
            datePicker.show()


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
            plan_Hour=getDate.get(Calendar.HOUR).toString()
            plan_Min=getDate.get(Calendar.MINUTE).toString()
        })

        //저장 버튼 구현
        button3.setOnClickListener{
            //여기에 파일 입력
            plan_Name=planName.text.toString()//제목가져오기
            Toast.makeText(this
                , plan_Year+"년"+plan_Month+"월"+plan_Day+"일 "+plan_Hour+"시"+plan_Min+"분"
                , Toast.LENGTH_SHORT ).show()
            val path = "/data/data/com.nyj.routinemaker/files/planfile"
            val writer = FileWriter(path, true)
            try{
                writer.write(plan_Year)
                writer.write(plan_Month)
                writer.write(plan_Day)
                writer.write(plan_Hour)
                writer.write(plan_Min)
                writer.write("\n")
            }catch (e: IOException){
            }finally {
                writer.close()
            }//아직미구현


            //val intent = Intent(this,MainActivity::class.java)
            //startActivity(intent)
        }
    }
}
