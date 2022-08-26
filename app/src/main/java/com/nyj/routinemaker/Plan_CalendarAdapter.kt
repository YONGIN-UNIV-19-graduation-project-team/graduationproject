package com.nyj.routinemaker

import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import java.time.LocalDate

class Plan_CalendarAdapter(private val dayList: ArrayList<LocalDate?>,
                            private val onItemListener: OnItemListener):
    RecyclerView.Adapter<Plan_CalendarAdapter.ItemViewHolder>() {


    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

    val dayText2: TextView = itemView.findViewById(R.id.dayText2)
}

    lateinit var pref_plan: SharedPreferences


    //화면 설정
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.calendar_item_2, parent, false)


        pref_plan = parent.context.getSharedPreferences("plan", 0)

        return ItemViewHolder(view)
    }

    //데이터 설정

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {

        //날짜 변수에 담기

        var day_position = dayList[holder.adapterPosition]

        var now_month = LocalDate.now().monthValue.toString()
        if (day_position == null) {
            holder.dayText2.text = ""
        } else {
            //해당 일자를 넣는다
            holder.dayText2.setText(day_position?.dayOfMonth.toString())
            println(day_position.dayOfMonth)
            //현재 날짜 색상 칠하기
            if ((day_position == CalendarUtil.selectDate) && (now_month == day_position?.monthValue.toString())) {
                holder.dayText2.setTextColor(Color.BLACK)
            }
        }


        //날짜 클릭 이벤트
        holder.itemView.setOnClickListener {

            onItemListener.onItemClick(day_position)
            //인터페이스를 통해 날짜를 넘겨준다.
            var iYear = day_position?.year
            var iMonth = day_position?.monthValue
            var iDay = day_position?.dayOfMonth

            var yearMonDay = "$iYear 년 $iMonth 월 $iDay 일"

            val editor_plan = pref_plan.edit()


            editor_plan.clear()
            editor_plan.putString("key_year", iYear.toString())
            editor_plan.putString("key_month", iMonth.toString())
            editor_plan.putString("key_day", iDay.toString())
            editor_plan.apply()


            Toast.makeText(holder.itemView.context, yearMonDay, Toast.LENGTH_SHORT).show()


        }






    }
    override fun getItemCount(): Int {
        return dayList.size
    }
}
