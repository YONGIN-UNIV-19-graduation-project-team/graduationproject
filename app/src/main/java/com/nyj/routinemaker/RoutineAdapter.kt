package com.nyj.routinemaker


import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Context.ALARM_SERVICE
import android.content.Intent
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.CheckBox
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.room.Room
import kotlinx.android.synthetic.main.fragment1.*
import java.security.AlgorithmParameters.getInstance
import java.text.Collator.getInstance
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.util.*

import java.util.Calendar.getInstance
import java.util.Currency.getInstance
import kotlin.collections.ArrayList


class RoutineAdapter(val context : Context, val RouineList : ArrayList<Routine>) : BaseAdapter()
{

    @RequiresApi(Build.VERSION_CODES.O)
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        //println("리스트뷰 객체 실행")
        val view: View = LayoutInflater.from(context).inflate(R.layout.list_item_routine, null)
        val name = view.findViewById<TextView>(R.id.rt_name)
        val hour = view.findViewById<TextView>(R.id.rt_hour)
        val min = view.findViewById<TextView>(R.id.rt_min)
        val dow = view.findViewById<TextView>(R.id.rt_dow)
        val chkbox = view.findViewById<CheckBox>(R.id.checkBox)


        val routineList = RouineList[position]
        val requestCode = routineList.id.toInt()
        name.text = routineList.name
        hour.text = routineList.hour
        min.text = routineList.min
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


//        /////////////////////////////알람구현/////////////////////////////
//        val alarmManager = context.getSystemService(ALARM_SERVICE) as AlarmManager
//        //시간구현 - LocalTime을 통한 시간 구현
//        val time = LocalTime.of(hour.text.toString()?.toInt(),min.text.toString()?.toInt())
//        val datetime = LocalDateTime.of(LocalDate.now(),time)
//        val conv_DateTime = datetime.atZone(ZoneId.of("Asia/Seoul"))
//        val conv_result = conv_DateTime.toInstant().toEpochMilli()
//        val triggerTime2 = conv_result
//        //시간구현 = Calendar을 통한 시간 구현
//        val triggerTime = Calendar.getInstance()
//        triggerTime.timeInMillis = System.currentTimeMillis()
//        triggerTime.set(Calendar.HOUR_OF_DAY,routineList.hour.toInt())
//        triggerTime.set(Calendar.MINUTE,routineList.min.toInt())
//        triggerTime.set(Calendar.SECOND,0)
//
//        //알람에 이 루틴의 정보 띄우기 위해서 정보 전달, requestCode 전달
//        val intent = Intent(context,AlarmReceiver::class.java)
//
//        intent.putExtra("title",(name.text.toString()))
//        intent.putExtra("time",(time.toString()))
//        intent.putExtra("requestCode",requestCode.toString())
//
//
//        val repeatInterval = AlarmManager.INTERVAL_DAY//반복시간설정
//        println("캘린더를 이용한 밀리초"+triggerTime.timeInMillis)
//        println("로컬타임을 이용한 밀리초"+triggerTime2)
//        if(!chkbox.isChecked) {//체크박스가 체크가 안되어있으면
//
//            val pendingIntent = PendingIntent.getBroadcast(
//                context,
//                requestCode,
//                intent,
//                PendingIntent.FLAG_UPDATE_CURRENT
//            )
//
//            alarmManager.setRepeating(
//                AlarmManager.RTC_WAKEUP,
//                triggerTime2,
//                repeatInterval,
//                pendingIntent
//            )
//        }
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