package com.nyj.routinemaker

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView


class RoutineAdapter(val context : Context, val RouineList : ArrayList<Routine>) : BaseAdapter()
{
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View = LayoutInflater.from(context).inflate(R.layout.list_item_routine, null)
        val name = view.findViewById<TextView>(R.id.rt_name)
        val hour = view.findViewById<TextView>(R.id.rt_hour)
        val min = view.findViewById<TextView>(R.id.rt_min)
        val dow = view.findViewById<TextView>(R.id.rt_dow)

        val routineList = RouineList[position]

        name.text = routineList.name
        hour.text = routineList.hour
        min.text = routineList.min
        dow.text = ""
        if(routineList.mon){dow.append(" 월")}
        if(routineList.tue){dow.append(" 화")}
        if(routineList.wed){dow.append(" 수")}
        if(routineList.thu){dow.append(" 목")}
        if(routineList.fri){dow.append(" 금")}
        if(routineList.sat){dow.append(" 토")}
        if(routineList.sun){dow.append(" 일")}


        return view
    }


    override fun getItem(position: Int): Any {
        return RouineList[position]
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    override fun getCount(): Int {
        return RouineList.size
    }


}