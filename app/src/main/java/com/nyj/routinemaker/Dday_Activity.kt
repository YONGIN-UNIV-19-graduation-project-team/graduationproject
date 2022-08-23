package com.nyj.routinemaker

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_addplan.*
import kotlinx.android.synthetic.main.activity_dday.*
import kotlinx.android.synthetic.main.activity_dday.textview_get_date
import kotlinx.android.synthetic.main.activity_main.*
import splitties.resources.int
import java.text.SimpleDateFormat
import java.util.*

class Dday_Activity : AppCompatActivity() {
    val formatDate = SimpleDateFormat("yyyy년 MM월 dd일", Locale.KOREA)
    var plan_Year=""
    var plan_Month=""
    var plan_Day=""
    var name = ""
    @SuppressLint("CommitPrefEdits")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dday)


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
            val pref = this.getSharedPreferences("a",0)
            val editor = pref.edit()

            editor.clear()
            editor.putString("key_year",plan_Year)
            editor.putString("key_month",plan_Month)
            editor.putString("key_day",plan_Day)
            editor.putString("key_name",name)
            editor.apply()
            startActivity(intent)
        }


    }
}