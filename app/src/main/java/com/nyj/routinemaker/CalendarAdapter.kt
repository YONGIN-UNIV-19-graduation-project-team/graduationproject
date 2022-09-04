package com.nyj.routinemaker

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import kotlinx.android.synthetic.main.calendar_item.view.*
import java.time.LocalDate

class CalendarAdapter(private val dayList: ArrayList<LocalDate?>):
    RecyclerView.Adapter<CalendarAdapter.ItemViewHolder>() {

    //챌린지 객체 생성
    var challengeList = arrayListOf<Challenge>(
        Challenge(0L,"2022","5","5",
            0.0)
    )

    class ItemViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){

        val dayText: TextView = itemView.findViewById(R.id.dayText)
    }

    //화면 설정
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.calendar_item, parent, false)

        val db = Room.databaseBuilder(
            parent.context.applicationContext,AppDatabase::class.java,"routine_databases"
        ).allowMainThreadQueries().build()
        //db에 연결하여 모든 챌린지를 challengeList 객체리스트에 저장한다.
        challengeList = db.challenge_DAO().getAll().toTypedArray().toCollection(ArrayList<Challenge>())

        db.close()
        return ItemViewHolder(view)
    }

    //데이터 설정
   @SuppressLint("ResourceAsColor")
   @RequiresApi(Build.VERSION_CODES.O)
   override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {


        //날짜 변수에 담기

        var day_position = dayList[holder.adapterPosition]
        //커스텀 캘린더를 생성하기 위한 년,월,일 값을 포지션의 메소드로 가져온다.
        var year = day_position?.year
        var month = day_position?.monthValue
        var day = day_position?.dayOfMonth

        //이번달
        var now_month = LocalDate.now().monthValue.toString()

        //db를 통해 가져온 모든 챌린지가 담긴 challengeList를 람다식을 사용하여 모든 날짜를 뷰에 띄우고
        //challenge객체의 해당 날짜의 챌린지 percent에 대한 색깔을 지정해준다.
        challengeList.forEach { challenge ->
            if (day_position == null) {
                holder.dayText.text = ""
            } else {
                //해당 일자를 넣는다
                holder.dayText.text = day_position.dayOfMonth.toString()
                if(challenge.year==year.toString()&&challenge.month==month.toString()&&challenge.day==day.toString()) {
                    if(challenge.percent==1.0){
                        holder.itemView.dayText.setBackgroundResource(R.drawable.challenge_background1)
                    }else if(challenge.percent>=0.75) {//0.75~0.999
                        holder.itemView.dayText.setBackgroundResource(R.drawable.challenge_background2)
                    }else if(challenge.percent>=0.5) {//0.5~0.7499
                        holder.itemView.dayText.setBackgroundResource(R.drawable.challenge_background3)
                    }else if(challenge.percent>=0.25) {//0.25~0.4999
                        holder.itemView.dayText.setBackgroundResource(R.drawable.challenge_background4)
                    }



                }
                //현재 날짜 색상 칠하기
                if ((day_position == CalendarUtil.selectDate) && (now_month == day_position?.monthValue.toString())) {

                    holder.dayText.setTextColor(Color.BLACK)
                }
            }
        }



        //날짜 클릭 이벤트
        holder.itemView.setOnClickListener{
            //인터페이스를 통해 날짜를 넘겨준다.
            var iYear = day_position?.year
            var iMonth = day_position?.monthValue
            var iDay = day_position?.dayOfMonth

            var yearMonDay = "$iYear 년 $iMonth 월 $iDay 일"

            Toast.makeText(holder.itemView.context, yearMonDay, Toast.LENGTH_SHORT).show()
        }
   }

    override fun getItemCount(): Int {
        return dayList.size
    }
}