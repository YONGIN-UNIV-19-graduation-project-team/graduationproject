package com.nyj.routinemaker



import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.CheckBox
import android.widget.TextView
import kotlin.collections.ArrayList


class RoutineAdapter(val context : Context, val RouineList : ArrayList<Routine>) : BaseAdapter()
{

    @SuppressLint("ResourceAsColor")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        //println("RoutineAdapter 실행")
        val view: View = LayoutInflater.from(context).inflate(R.layout.list_item_routine, null)
        val name = view.findViewById<TextView>(R.id.rt_name)
        val hour = view.findViewById<TextView>(R.id.rt_hour)
        val min = view.findViewById<TextView>(R.id.rt_min)

        val chkbox = view.findViewById<CheckBox>(R.id.checkBox)

        val dow1 = view.findViewById<TextView>(R.id.rt_1)
        val dow2 = view.findViewById<TextView>(R.id.rt_2)
        val dow3 = view.findViewById<TextView>(R.id.rt_3)
        val dow4 = view.findViewById<TextView>(R.id.rt_4)
        val dow5 = view.findViewById<TextView>(R.id.rt_5)
        val dow6 = view.findViewById<TextView>(R.id.rt_6)
        val dow7 = view.findViewById<TextView>(R.id.rt_7)


        val routineList = RouineList[position]

        name.text = routineList.name

        if(routineList.hour.toInt()<10) {
            hour.text = "0"+routineList.hour
        }else hour.text = routineList.hour

        if(routineList.min.toInt()<10){
            min.text = "0"+routineList.min
        }else min.text = routineList.min




        if(routineList.routineischecked){chkbox.isChecked=true}//체크박스 상태 띄으기
        if(!routineList.routineischecked){chkbox.isChecked=false}


        if(routineList.mon){dow1.setTextColor(Color.BLACK)}
        if(routineList.tue){dow2.setTextColor(Color.BLACK)}
        if(routineList.wed){dow3.setTextColor(Color.BLACK)}
        if(routineList.thu){dow4.setTextColor(Color.BLACK)}
        if(routineList.fri){dow5.setTextColor(Color.BLACK)}
        if(routineList.sat){dow6.setTextColor(Color.BLACK)}
        if(routineList.sun){dow7.setTextColor(Color.BLACK)}



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