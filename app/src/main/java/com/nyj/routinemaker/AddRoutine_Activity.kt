package com.nyj.routinemaker

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.TimePicker
import android.widget.Toast
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
    val weekArray = Array<ByteArray>(7){ byteArrayOf()}
    override fun onCreate(savedInstanceState: Bundle?) {//
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_addroutine)

        //var cal = Calendar.getInstance()
        //val hour = cal.get(Calendar.HOUR_OF_DAY)
        //val minute = cal.get(Calendar.MINUTE)
        //val textView = findViewById<TextView>(R.id.timetext)
        val timePicker = findViewById<TimePicker>(R.id.timePicker)

        //textView.text = "현재 설정된 시간 : $hour : $minute"//화면 딱 띄웠을때 현재시간 나타나게 하는 코드
        timePicker.setOnTimeChangedListener(this)





        //추가 버튼 클릭시 이벤트
        add_button.setOnClickListener{

            //체크박스 요일 리스트에 저장 아직 구현 못함
            if(checkBox1.isChecked){weekArray[0] = "mon".toByteArray()}
            if(checkBox1.isChecked){weekArray[1] = "tue".toByteArray()}
            if(checkBox1.isChecked){weekArray[2] = "wed".toByteArray()}
            if(checkBox1.isChecked){weekArray[3] = "thu".toByteArray()}
            if(checkBox1.isChecked){weekArray[4] = "fri".toByteArray()}
            if(checkBox1.isChecked){weekArray[5] = "sat".toByteArray()}
            if(checkBox1.isChecked){weekArray[6] = "sun".toByteArray()}

            //Toast.makeText(this, "데이터전송테스트\narray: "+weekArray[0],Toast.LENGTH_SHORT ).show()

            //변수저장
            // MainActivity로 이동하기 위한 intent. 데이터 전송은 덤(Toast)
            //
            val intent = Intent(this,MainActivity::class.java)
            intent.putExtra("hour",changed_hour.toString())
            intent.putExtra("minute",changed_minute.toString())
            routine_name = routine_Name.text.toString()
            intent.putExtra("name",routine_name)
            //

            //파일 접근, 저장.
            //바이트 기반 스트림
            var outputfile : FileOutputStream = openFileOutput("routinefile", MODE_PRIVATE)//MODE_APPEND로 변경해야함
            outputfile.write(
                changed_hour.toString().toByteArray()+
                    changed_minute.toString().toByteArray()+
                    routine_name.toString().toByteArray()
                        )
            outputfile.write("\n".toString().toByteArray())
            outputfile.close()
            startActivity(intent)

        }

    }

    override fun onTimeChanged(view: TimePicker?, hourOfDay: Int, minute: Int) {
        val textView = findViewById<TextView>(R.id.timetext)
        textView.text = "현재 설정된 시간 : $hourOfDay : $minute"
        changed_hour=hourOfDay
        changed_minute=minute
    }

}