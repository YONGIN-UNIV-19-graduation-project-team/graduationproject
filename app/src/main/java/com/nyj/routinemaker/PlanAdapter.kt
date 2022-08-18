package com.nyj.routinemaker

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView

class PlanAdapter (val context : Context, val PlanList : ArrayList<Plan>) : BaseAdapter()
{
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        println("planAdapter 실행")
        val view: View = LayoutInflater.from(context).inflate(R.layout.list_item_plan, null)
        val name = view.findViewById<TextView>(R.id.plan_name)
        val data = view.findViewById<TextView>(R.id.plan_data)
        var hour = ""
        var min = ""
        val planList = PlanList[position]

        if(planList.hour.toInt()<10) {
            hour = "0"+planList.hour
        }else hour = planList.hour

        if(planList.min.toInt()<10){
            min = "0"+planList.min
        }else min = planList.min


        name.text = planList.name
        data.text = hour+"시 " +min+"분"



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