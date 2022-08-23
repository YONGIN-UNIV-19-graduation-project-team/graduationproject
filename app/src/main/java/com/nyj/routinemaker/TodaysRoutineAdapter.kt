package com.nyj.routinemaker

import android.content.Context
import android.graphics.Color
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

        if(routineList.hour.toInt()<12) {
            time.text = "오전 ${routineList.hour}시 ${routineList.min}분"
        }
        else time.text = "오후 ${(routineList.hour.toInt()-12)}시 ${routineList.min}분"



        name.text = routineList.name
        //루틴이 수행된거면 중간줄
        if(routineList.routineischecked) {
            name.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG)
            time.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG)
            //색도 변경(gray)
            name.setTextColor(Color.GRAY)
            time.setTextColor(Color.GRAY)
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