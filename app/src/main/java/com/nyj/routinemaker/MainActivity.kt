package com.nyj.routinemaker

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment1.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //루틴추가 시 받아온 데이터 hour, minute, name
        var intent = getIntent()
        var hour = intent.getStringExtra("hour").toString()
        var minute = intent.getStringExtra("minute").toString()
        var name = intent.getStringExtra("name").toString()
        Toast.makeText(this, "데이터전송테스트\nhour: "+hour+ "\nminute: "+minute+ "\nname: "+name, Toast.LENGTH_SHORT ).show()
        //

        setFrag(0)

        frg1_button.setOnClickListener{
            setFrag(0)
        }
        frg2_button.setOnClickListener{
            setFrag(1)
        }


    }

    private fun setFrag(fragNum : Int) {
        //fragnum이 0일때 fragment1로 , 1때 fragment2로
       val ft = supportFragmentManager.beginTransaction()
        when (fragNum){
            0 -> {
                ft.replace(R.id.main_frame, Fragment1()).commit()
            }
            1 -> {
                ft.replace(R.id.main_frame, Fragment2()).commit()
            }
        }
    }




}





