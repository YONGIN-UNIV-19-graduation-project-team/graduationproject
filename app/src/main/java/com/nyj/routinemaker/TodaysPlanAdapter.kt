package com.nyj.routinemaker

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_addplan.*

class TodaysPlanAdapter (val context: Context,val PlanList:ArrayList<Plan>):BaseAdapter(){
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view:View = LayoutInflater.from(context).inflate(R.layout.list_item_todays_plan,null)
        val name = view.findViewById<TextView>(R.id.todays_plan_name)
        val time = view.findViewById<TextView>(R.id.todays_plan_time)
        val planList = PlanList[position]
        name.text = planList.name

        //오전 오후 나타내고 싶어서 짠 코드
        if(planList.hour.toInt()<12) {
            time.text = "오전 ${planList.hour}시 ${planList.min}분"
        }
        else time.text = "오후 ${(planList.hour.toInt()-12)}시 ${planList.min}분"



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