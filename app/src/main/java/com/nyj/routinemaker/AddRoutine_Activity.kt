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
            var outputfile : FileOutputStream = openFileOutput("routinefile", MODE_PRIVATE)//MODE_APPEND로 변경해야함(누적)
            outputfile.write(
                changed_hour.toString().toByteArray()+
                    changed_minute.toString().toByteArray()+
                    routine_name.toByteArray()
                        )
            outputfile.write(" ".toByteArray())
            if(checkBox1.isChecked){
                outputfile.write("mon".toByteArray())
                outputfile.write(" ".toByteArray())
            }
            if(checkBox2.isChecked){
                outputfile.write("tue".toByteArray())
                outputfile.write(" ".toByteArray())
            }
            if(checkBox3.isChecked){
                outputfile.write("wed".toByteArray())
                outputfile.write(" ".toByteArray())
            }
            if(checkBox4.isChecked){
                outputfile.write("thu".toByteArray())
                outputfile.write(" ".toByteArray())
            }
            if(checkBox5.isChecked){
                outputfile.write("fri".toByteArray())
                outputfile.write(" ".toByteArray())
            }
            if(checkBox6.isChecked){
                outputfile.write("sat".toByteArray())
                outputfile.write(" ".toByteArray())
            }
            if(checkBox7.isChecked){
                outputfile.write("sun".toByteArray())
            }
            outputfile.write("\n".toByteArray())
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