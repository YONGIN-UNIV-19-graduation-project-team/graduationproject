package com.nyj.routinemaker

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.TimePicker
import kotlinx.android.synthetic.main.activity_addroutine.*
import java.io.DataOutputStream
import java.io.File
import java.io.FileOutputStream
import java.util.*

class AddRoutine_Activity : AppCompatActivity() , TimePicker.OnTimeChangedListener{
    //val timePicker = findViewById<TimePicker>(R.id.timePicker)
    var changed_hour=0
    var changed_minute=0
    var routine_name=""
    override fun onCreate(savedInstanceState: Bundle?) {//
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_addroutine)

        //var cal = Calendar.getInstance()
        //val hour = cal.get(Calendar.HOUR_OF_DAY)
        //val minute = cal.get(Calendar.MINUTE)
        //val textView = findViewById<TextView>(R.id.timetext)
        val timePicker = findViewById<TimePicker>(R.id.timePicker)

        //textView.text = "현재 설정된 시간 : $hour : $minute"//
        timePicker.setOnTimeChangedListener(this)


        add_button.setOnClickListener{//추가 버튼 클릭시 이벤트

            val intent = Intent(this,MainActivity::class.java)
            intent.putExtra("hour",changed_hour.toString())
            intent.putExtra("minute",changed_minute.toString())
            routine_name = routine_Name.text.toString()
            intent.putExtra("name",routine_name)
            //파일 접근, 저장 테스트

            var outputfile : FileOutputStream = openFileOutput("routinefile", MODE_APPEND)
            outputfile.write(changed_hour.toString().toByteArray()+changed_minute.toString().toByteArray()+routine_name.toString().toByteArray())
            outputfile.write("\n".toString().toByteArray())
            outputfile.close()
            ////브랜치테스트
            startActivity(intent)

        }

    }

    override fun onTimeChanged(view: TimePicker?, hourOfDay: Int, minute: Int) {
        val textView = findViewById<TextView>(R.id.timetext)
        textView.text = "현재 설정된 시간 : $hourOfDay : $minute"
        //timePicker.setOnTimeChangedListener(this)
        changed_hour=hourOfDay
        changed_minute=minute
    }

}