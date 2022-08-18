package com.nyj.routinemaker



import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.CheckBox
import android.widget.TextView
import kotlin.collections.ArrayList


class RoutineAdapter(val context : Context, val RouineList : ArrayList<Routine>) : BaseAdapter()
{

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        println("RoutineAdapter 실행")
        val view: View = LayoutInflater.from(context).inflate(R.layout.list_item_routine, null)
        val name = view.findViewById<TextView>(R.id.rt_name)
        val hour = view.findViewById<TextView>(R.id.rt_hour)
        val min = view.findViewById<TextView>(R.id.rt_min)
        val dow = view.findViewById<TextView>(R.id.rt_dow)
        val chkbox = view.findViewById<CheckBox>(R.id.checkBox)


        val routineList = RouineList[position]

        name.text = routineList.name

        if(routineList.hour.toInt()<10) {
            hour.text = "0"+routineList.hour
        }else hour.text = routineList.hour

        if(routineList.min.toInt()<10){
            min.text = "0"+routineList.min
        }else min.text = routineList.min


        dow.text = ""

        if(routineList.routineischecked){chkbox.isChecked=true}//체크박스 상태 띄으기
        else{chkbox.isChecked==false}//될지 모름


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