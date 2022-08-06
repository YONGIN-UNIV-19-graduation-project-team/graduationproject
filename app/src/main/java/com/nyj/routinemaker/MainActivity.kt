package com.nyj.routinemaker

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment1.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


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
                frg1_button.setTextColor(getResources().getColor(R.color.blue, getResources().newTheme()))
                frg2_button.setTextColor(Color.GRAY)

            }
            1 -> {
                ft.replace(R.id.main_frame, Fragment2()).commit()
                frg1_button.setTextColor(Color.GRAY)
                frg2_button.setTextColor(getResources().getColor(R.color.blue, getResources().newTheme()))

            }
        }
    }




}





