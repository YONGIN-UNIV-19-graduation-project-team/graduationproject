package com.nyj.routinemaker

import android.content.Context
import android.graphics.Paint
import android.text.SpannableString
import android.text.style.StrikethroughSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView

class TodaysRoutineAdapter (val context: Context,val RoutineList:ArrayList<Routine>):BaseAdapter(){
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view:View = LayoutInflater.from(context).inflate(R.layout.list_item_todays_routine,null)
        val name = view.findViewById<TextView>(R.id.todays_routine_name)
        val time = view.findViewById<TextView>(R.id.todays_routine_time)

        val routineList = RoutineList[position]

        val nametext = routineList.name

        if(routineList.hour.toInt()<10&&routineList.min.toInt()<10) {
            time.text = "확인 시간 0${routineList.hour} : 0${routineList.min}"
        }
        else {
            if (routineList.min.toInt()<10) {
                time.text = "확인 시간 ${routineList.hour} : 0${routineList.min}"
            }
            else
                if(routineList.hour.toInt()<10) {
                    time.text = "확인 시간 0${routineList.hour} : ${routineList.min}"
                }
                else time.text = "확인 시간 ${routineList.hour} : ${routineList.min}"
        }




        name.text = routineList.name
        //루틴이 수행된거면 중간줄
        if(routineList.routineischecked) {
            name.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG)
            time.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG)
            //색도 변경(gray)
            ///
        }


        return view
    }

    override fun getItem(position: Int): Any {
        return RoutineList[position]
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    override fun getCount(): Int {
        return RoutineList.size
    }
}