package com.nyj.routinemaker

import android.content.Context
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
        name.text = routineList.name
        val timetext = "확인 시간 ${routineList.hour} : ${routineList.min}"
        time.text = timetext
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