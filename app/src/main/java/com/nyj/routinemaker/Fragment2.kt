package com.nyj.routinemaker

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment2.*

class Fragment2 : Fragment(){
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        //xml과 연결
        val view = inflater.inflate(R.layout.fragment2, container, false)

        return view
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //val pass : Button by lazy { calendarView.setOnClickListener}
        setOnClickListener()
    }

    fun setOnClickListener(){//값 옮기기 Fragment2->AddPlan_Activity로 year,month,day값 보내줘야함.
        //commit test 0730...
        calendarView.setOnDateChangeListener{view, year, month, dayofMonth ->

            val intent = Intent(activity, AddPlan_Activity::class.java)
            intent.apply {
                intent.putExtra("year", year.toString())//toString으로 형변환 해야 null값이 아닌채로 데이터 전송.0730
                intent.putExtra("month", month.toString())
                intent.putExtra("day", dayofMonth.toString())

                startActivity(intent)


            }
        }

    }

}
