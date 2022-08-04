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

//시현

        var intent = getIntent()

        var year = intent.getStringExtra("year").toString()
        //var month = intent.getStringExtra("month").toString()
        var day = intent.getStringExtra("day").toString()
        var month = ((intent.getStringExtra("month"))!!.toInt()+1).toString()//자꾸 1달 낮게 데이터가 넘겨짐.
        // ex)8월 달력 클릭시 7월로 데이터가 이전되어서 형변환후 1추가시킨후 다시형변환했음

        //toast로 데이터 넘기기 테스트
        //Toast.makeText(this, "year : "+year, Toast.LENGTH_SHORT ).show()

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

        //시간 선택 란 //미해결
        btn_pick_time.setOnClickListener(View.OnClickListener {
            val getDate = Calendar.getInstance()
            val datePicker = TimePickerDialog(this,android.R.style.Theme_Holo_Light_Dialog_MinWidth, TimePickerDialog.OnTimeSetListener { timePicker, i, i2->
                val selectDate = Calendar.getInstance()
                selectDate.set(Calendar.HOUR,i)
                selectDate.set(Calendar.MINUTE,i2)//
                val date = formatTime.format(selectDate.time)
                Toast.makeText(this, "Time : "+date, Toast.LENGTH_SHORT ).show()
                textview_get_time.text = date
                plan_Hour=selectDate.get(Calendar.HOUR).toString()
                plan_Min=selectDate.get(Calendar.MINUTE).toString()

            },getDate.get(Calendar.HOUR),getDate.get(Calendar.MINUTE),true)
            datePicker.show()
//            AddPlan_Activity에서 시간 선택 란에서 오류가 나타남. 시간,분 선택 시 시간은 오류가 없으나 분에서 자꾸 8분으로 고정되서 나타나는 버그.
//            그 외에는 작동 잘되고 데이터 전송은 오류없음.(선택된 시간,분 데이터가 전달이 됨)
        })

        //저장 버튼 구현. 바이트형변환 테스트 때문에 AddRoutine액티비티와 다른 api를 사용해봤음. 이 방식은 바이트형변환을 하지 않음.
        button3.setOnClickListener{
            //여기에 파일 입력
            plan_Name=planName.text.toString()//제목가져오기
            Toast.makeText(this
                , plan_Year+"년"+plan_Month+"월"+plan_Day+"일 "+plan_Hour+"시"+plan_Min+"분"
                , Toast.LENGTH_SHORT ).show()
            val path = "/data/data/com.nyj.routinemaker/files/planfile"
            //문자 기반 스트림
            val writer = FileWriter(path)//매개변수 추가로 true를 해주면 덮어쓰기.
            //val writer = FileWriter(path,true)
            try{
                writer.write(plan_Name)
                writer.write(" ")
                writer.write(plan_Year)
                writer.write("년")//데이터 받을땐 이 텍스트들 주석처리 해야함
                writer.write(plan_Month)
                writer.write("월")//
                writer.write(plan_Day)
                writer.write("일 ")//
                writer.write(plan_Hour)
                writer.write("시")//
                writer.write(plan_Min)
                writer.write("분")//
                writer.write("\n")//이건주석처리 x
            }catch (e: IOException){
            }finally {
                writer.close()
            }//아직미구현


            //val intent = Intent(this,MainActivity::class.java)
            //startActivity(intent)
        }
    }
}
