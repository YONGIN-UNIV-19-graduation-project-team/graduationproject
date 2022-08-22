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

        if(planList.hour.toInt()<10&&planList.min.toInt()<10) {
            time.text = "확인 시간 0${planList.hour} : 0${planList.min}"
        }
        else {
            if (planList.min.toInt()<10) {
                time.text = "확인 시간 ${planList.hour} : 0${planList.min}"
            }
            else
                if(planList.hour.toInt()<10) {
                    time.text = "확인 시간 0${planList.hour} : ${planList.min}"
                }
                else time.text = "확인 시간 ${planList.hour} : ${planList.min}"
        }



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