package com.nyj.routinemaker



import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.CheckBox
import android.widget.TextView
import androidx.core.content.ContextCompat
import kotlin.collections.ArrayList


class RoutineAdapter(val context : Context, val RoutineList : ArrayList<Routine>) : BaseAdapter()
{

    @SuppressLint("ResourceAsColor")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        //뷰바인딩안하고 그냥 뷰 연결
        val view: View = LayoutInflater.from(context).inflate(R.layout.list_item_routine, null)
        val name = view.findViewById<TextView>(R.id.rt_name)
        val hour = view.findViewById<TextView>(R.id.rt_hour)
        val min = view.findViewById<TextView>(R.id.rt_min)

        //체크박스 구현(클릭안되는 체크박스임)
        val chkbox = view.findViewById<CheckBox>(R.id.checkBox)

        //월화수목금토일
        val dow1 = view.findViewById<TextView>(R.id.rt_1)
        val dow2 = view.findViewById<TextView>(R.id.rt_2)
        val dow3 = view.findViewById<TextView>(R.id.rt_3)
        val dow4 = view.findViewById<TextView>(R.id.rt_4)
        val dow5 = view.findViewById<TextView>(R.id.rt_5)
        val dow6 = view.findViewById<TextView>(R.id.rt_6)
        val dow7 = view.findViewById<TextView>(R.id.rt_7)

        //어댑터 연결
        val routineList = RoutineList[position]

        //텍스트 띄우기
        name.text = routineList.name

        //00시 00분으로 나타내고 싶어서 짠 코드
        if(routineList.hour.toInt()<10) {
            hour.text = "0"+routineList.hour
        }else hour.text = routineList.hour

        if(routineList.min.toInt()<10){
            min.text = "0"+routineList.min
        }else min.text = routineList.min

        //수행한 루틴이면
        if(routineList.routineischecked) {
            //루틴 수행 시 중간줄 긋기
            name.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG)
            //색도 변경(gray)
            name.setTextColor(Color.GRAY)
        }else name.setPaintFlags(0)


        //체크박스 상태 띄으기
        if(routineList.routineischecked){chkbox.isChecked=true}
        if(!routineList.routineischecked){chkbox.isChecked=false}

        //루틴의 해당하는 요일 색 빨간색으로 변경
        if(routineList.mon){dow1.setTextColor(ContextCompat.getColor(context, R.color.red))}
        if(routineList.tue){dow2.setTextColor(ContextCompat.getColor(context, R.color.red))}
        if(routineList.wed){dow3.setTextColor(ContextCompat.getColor(context, R.color.red))}
        if(routineList.thu){dow4.setTextColor(ContextCompat.getColor(context, R.color.red))}
        if(routineList.fri){dow5.setTextColor(ContextCompat.getColor(context, R.color.red))}
        if(routineList.sat){dow6.setTextColor(ContextCompat.getColor(context, R.color.red))}
        if(routineList.sun){dow7.setTextColor(ContextCompat.getColor(context, R.color.red))}



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