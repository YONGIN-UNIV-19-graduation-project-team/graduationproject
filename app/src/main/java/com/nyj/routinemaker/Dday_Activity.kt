package com.nyj.routinemaker

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_dday.*
import kotlinx.android.synthetic.main.activity_dday.textview_get_date
import java.text.SimpleDateFormat
import java.util.*

class Dday_Activity : AppCompatActivity() {
    val formatDate = SimpleDateFormat("yyyy년 M월 dd일", Locale.KOREA)
    var plan_Year=""
    var plan_Month=""
    var plan_Day=""

    @SuppressLint("CommitPrefEdits")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dday)
        val pref = this.getSharedPreferences("a",0)
        var name = pref.getString("key_name","null").toString()
        d_day_name.setText(name)
        var year = pref.getString("key_year","null").toString()
        var month = pref.getString("key_month","null").toString()
        var day = pref.getString("key_day","null").toString()


        if(year!="null"&&month!="null"&&day!="null") {textview_get_date.text = year+"년 "+month+"월 "+day+"일"}

        textview_get_date.setOnClickListener(View.OnClickListener {
            val getDate = Calendar.getInstance()
            val datePicker = DatePickerDialog(this,android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                DatePickerDialog.OnDateSetListener { datePicker, i, i2, i3 ->

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





        add_dday_button.setOnClickListener{
            name = d_day_name.text.toString()
            val intent = Intent(this,MainActivity::class.java)
            val editor = pref.edit()

            editor.clear()
            editor.putString("key_year",plan_Year)
            editor.putString("key_month",plan_Month)
            editor.putString("key_day",plan_Day)
            editor.putString("key_name",name)
            editor.apply()
            startActivity(intent)
        }

        can_button.setOnClickListener{
            val intent_can = Intent(this,MainActivity::class.java)
            startActivity(intent_can)
        }
    }
}