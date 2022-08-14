package com.nyj.routinemaker


import android.app.Activity
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Context.ALARM_SERVICE
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.CheckBox
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import kotlinx.android.synthetic.main.fragment1.*
import java.security.AlgorithmParameters.getInstance
import java.text.Collator.getInstance
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId

import java.util.Calendar.getInstance
import java.util.Currency.getInstance


class RoutineAdapter(val context : Context, val RouineList : ArrayList<Routine>) : BaseAdapter()
{

    @RequiresApi(Build.VERSION_CODES.O)
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View = LayoutInflater.from(context).inflate(R.layout.list_item_routine, null)
        val name = view.findViewById<TextView>(R.id.rt_name)
        val hour = view.findViewById<TextView>(R.id.rt_hour)
        val min = view.findViewById<TextView>(R.id.rt_min)
        val dow = view.findViewById<TextView>(R.id.rt_dow)
        val chkbox = view.findViewById<CheckBox>(R.id.checkBox)
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

        val alarmManager = context.getSystemService(ALARM_SERVICE) as AlarmManager


        val time = LocalTime.of(hour.text.toString()?.toInt(),min.text.toString()?.toInt())
        val datetime = LocalDateTime.of(LocalDate.now(),time)
        val conv_DateTime = datetime.atZone(ZoneId.of("Asia/Seoul"))
        val conv_result = conv_DateTime.toInstant().toEpochMilli()


        val intent = Intent(context,AlarmReceiver::class.java)

        intent.putExtra("title",(name.text.toString()))

        val pendingIntent = PendingIntent.getBroadcast(context,System.currentTimeMillis().toInt(),intent,PendingIntent.FLAG_UPDATE_CURRENT)

        val repeatInterval = AlarmManager.INTERVAL_DAY
        //val triggerTime = (conv_result)//알람을 울릴 시간 설정
        //alarmManager.setRepeating( AlarmManager.RTC_WAKEUP, triggerTime,repeatInterval, pendingIntent )
        chkbox.setOnCheckedChangeListener{_,check->
            val toastMessage = if(check){
                val triggerTime = (conv_result)//알람을 울릴 시간 설정
                alarmManager.setRepeating( AlarmManager.RTC_WAKEUP, triggerTime,repeatInterval, pendingIntent )
                "$time 알람이 설정되었습니다"
            }else{alarmManager.cancel(pendingIntent)
                "알람 설정 취소함"
            }
            Toast.makeText(context,toastMessage,Toast.LENGTH_SHORT).show()
        }






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