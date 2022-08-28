package com.nyj.routinemaker

import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import java.time.LocalDate


class Plan_CalendarAdapter(private val dayList: ArrayList<LocalDate?>,
                            private val onItemListener: OnItemListener):
    RecyclerView.Adapter<Plan_CalendarAdapter.ItemViewHolder>() {

    var pos = 0//선택한 포지션 빨간색으로 바꾸기위한 변수.

    //빈 Plan 객체리스트 초기화
    var PlanList = arrayListOf<Plan>(
        Plan(
            0L, "더미", "2022", "11",
            "12", "0", "0", ""
        )
    )

    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

    val dayText2: TextView = itemView.findViewById(R.id.dayText2)
    val dot:ImageView = itemView.findViewById(R.id.dot)
}

    //lateinit var pref_plan: SharedPreferences


    //화면 설정
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.calendar_item_2, parent, false)


        //pref_plan = parent.context.getSharedPreferences("plan", 0)

        val db = Room.databaseBuilder(
            parent.context.applicationContext,AppDatabase::class.java,"routine_databases"
        ).allowMainThreadQueries().build()
        PlanList = db.plan_DAO().getAll().toTypedArray().toCollection(ArrayList<Plan>())

        db.close()
        return ItemViewHolder(view)
    }

    //데이터 설정

    @SuppressLint("NotifyDataSetChanged")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {

        //날짜 변수에 담기
        var day_position = dayList[holder.adapterPosition]
        var now_month = LocalDate.now().monthValue.toString()

        //캘린더의 날짜들 뷰에 띄운다
        if (day_position == null) {
            holder.dayText2.text = ""
        } else {
            //해당 일자를 넣는다
            holder.dayText2.setText(day_position?.dayOfMonth.toString())
            //현재 날짜 색상 칠하기
            if ((day_position == CalendarUtil.selectDate) && (now_month == day_position?.monthValue.toString())) {
                holder.dayText2.setTextColor(Color.BLACK)
            }
        }


        //선택한 포지션을 빨간색으로 변경
        if(pos!=0&&position==pos){
            holder.dayText2.setTextColor(Color.RED)
        }

        ////////////////람다식으로 해당날짜 비교하여 할일이 존재하는 날짜에 도트 visible로 보이게 함
        PlanList.forEach { planlist->
            if(day_position?.year==planlist.year.toInt()&&day_position?.monthValue==planlist.month.toInt()&&day_position?.dayOfMonth==planlist.day.toInt()){
                //도트 구현
                holder.dot.setVisibility(View.VISIBLE)


            }
        }






        //날짜 클릭 이벤트
        holder.itemView.setOnClickListener {
            pos = holder.adapterPosition//선택한 포지션을 pos변수에 넣음
            this.notifyDataSetChanged()//뷰 재설정


//            println(click)
//            if(click) {
//                this.notifyDataSetChanged()
//                click=false
//                holder.dayText2.setTextColor(Color.RED)
//            }else{
//                holder.dayText2.setTextColor(Color.RED)
//                click=true
//            }
//            println(click)







            ////////클릭한 텍스트 빨간색으로 변경////////



            //click=true

            //인터페이스를 사용하기 위한 코드.
            onItemListener.onItemClick(day_position)
            //인터페이스를 통해 날짜를 넘겨준다.
            var iYear = day_position?.year
            var iMonth = day_position?.monthValue
            var iDay = day_position?.dayOfMonth
            //toast를 위한 String 포맷
            var yearMonDay = "$iYear 년 $iMonth 월 $iDay 일"

//            val editor_plan = pref_plan.edit()
//
//
//
//            editor_plan.clear()
//            editor_plan.putString("key_year", iYear.toString())
//            editor_plan.putString("key_month", iMonth.toString())
//            editor_plan.putString("key_day", iDay.toString())
//            //editor_plan.putBoolean("click?",click)
//            editor_plan.apply()


            Toast.makeText(holder.itemView.context, yearMonDay, Toast.LENGTH_SHORT).show()
            //holder.itemView.setBackgroundResource(R.drawable.challenge_background4)

        }






    }
    override fun getItemCount(): Int {
        return dayList.size
    }

}
