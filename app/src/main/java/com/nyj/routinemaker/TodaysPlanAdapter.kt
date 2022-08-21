package com.nyj.routinemaker

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView

class TodaysPlanAdapter (val context: Context,val PlanList:ArrayList<Plan>):BaseAdapter(){
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view:View = LayoutInflater.from(context).inflate(R.layout.list_item_todays_plan,null)
        val name = view.findViewById<TextView>(R.id.todays_plan_name)
        val time = view.findViewById<TextView>(R.id.todays_plan_time)
        val planList = PlanList[position]
        name.text = planList.name
        val timetext = "확인 시간 ${planList.hour} : ${planList.min}"
        time.text = timetext
        return view
    }

    override fun getItem(position: Int): Any {
        return PlanList[position]
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    override fun getCount(): Int {
        return PlanList.size
    }
}