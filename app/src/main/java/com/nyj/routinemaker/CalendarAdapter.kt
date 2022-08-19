package com.nyj.routinemaker

import android.graphics.Color
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat.getColor
import androidx.recyclerview.widget.RecyclerView
import com.nyj.routinemaker.CalendarUtil.Companion.selectDate
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class CalendarAdapter(private val dayList: ArrayList<LocalDate?>):
    RecyclerView.Adapter<CalendarAdapter.ItemViewHolder>() {

    class ItemViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){

        val dayText: TextView = itemView.findViewById(R.id.dayText)
    }
    //화면 설정
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.calendar_item, parent, false)
        return ItemViewHolder(view)
    }

    //데이터 설정
   @RequiresApi(Build.VERSION_CODES.O)
   override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        println("뷰바인더실행")
        //날짜 변수에 담기
        var day = dayList[holder.adapterPosition]
        var month = LocalDate.now().monthValue.toString()

        if(day==null){
            holder.dayText.text = ""
        }else{
            //해당 일자를 넣는다
            holder.dayText.text = day.dayOfMonth.toString()
            //현재 날짜 색상 칠하기
            if((day == CalendarUtil.selectDate) && (month == day?.monthValue.toString()))
            {
                println("월검색조건문")
                holder.itemView.setBackgroundColor(Color.BLUE)
            }
        }

        //텍스트 색상 지정(토,일)
        if((position+1) % 7 == 0) {//토요일은 파랑
            holder.dayText.setTextColor(Color.BLUE)
        }else if(position == 0 || position % 7 == 0){//일요일은 빨강
            holder.dayText.setTextColor(Color.RED)
        }

        //날짜 클릭 이벤트
        holder.itemView.setOnClickListener{
            //인터페이스를 통해 날짜를 넘겨준다.
            var iYear = day?.year
            var iMonth = day?.monthValue
            var iDay = day?.dayOfMonth

            var yearMonDay = "$iYear 년 $iMonth 월 $iDay 일"

            Toast.makeText(holder.itemView.context, yearMonDay, Toast.LENGTH_SHORT).show()
        }
   }

    override fun getItemCount(): Int {
        return dayList.size
    }
}