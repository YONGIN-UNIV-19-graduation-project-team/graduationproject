package com.nyj.routinemaker

import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.nyj.routinemaker.databinding.Fragment2Binding
import kotlinx.android.synthetic.main.calendar_item_2.*
import kotlinx.android.synthetic.main.fragment2.*
import kotlinx.android.synthetic.main.fragment2.listView
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter

class Fragment2 : Fragment(),OnItemListener {
    private lateinit var binding: Fragment2Binding
    var PlanList = arrayListOf<Plan>(
        Plan(
            0L, "더미", "2022", "11",
            "12", "0", "0", ""
        )
    )
    var date_ = ""
    var clicked_year = "2022"
    var clicked_month = "8"
    var clicked_dayofMonth = "20"
    var count = 0

    lateinit var pref_plan:SharedPreferences

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //xml과 연결
//        val view = inflater.inflate(R.layout.fragment2, container, false)
        binding = Fragment2Binding.inflate(inflater, container, false)
        clicked_year= LocalDate.now().year.toString()
        clicked_month= LocalDate.now().monthValue.toString()
        clicked_dayofMonth= LocalDate.now().dayOfMonth.toString()
        date_ = clicked_year+clicked_month+clicked_dayofMonth
        println(date_)
        //db연결
        val db = Room.databaseBuilder(
            requireActivity().applicationContext, AppDatabase::class.java, "routine_databases"
        ).allowMainThreadQueries().build()
        PlanList = db.plan_DAO().getPlanbyDate(date_).toTypedArray().toCollection(ArrayList<Plan>())
        count = db.plan_DAO().countPlan(date_)
        db.close()


        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        pref_plan = requireActivity().applicationContext.getSharedPreferences("plan",0)



        listcount.text = count.toString()+"개"


        val Adapter = PlanAdapter(requireContext(), PlanList)
        listView.adapter = Adapter

        listView.onItemClickListener =
            AdapterView.OnItemClickListener { parent, view, position, id ->
                val selectItem = parent.getItemAtPosition(position) as Plan

                val intent = Intent(activity, ModPlan_Activity::class.java)
                intent.apply {
                    intent.putExtra(
                        "planid",
                        selectItem.id
                    )


                    startActivity(intent)

                }

            }
        /////////////////////////////커스텀달력구현부///////////////////////
        setMonthview()

        pre_btn_pl.setOnClickListener {
            CalendarUtil.selectDate = CalendarUtil.selectDate.minusMonths(1)
            setMonthview()
        }

        //다음달 버튼 이벤트
        next_btn_pl.setOnClickListener {
            CalendarUtil.selectDate = CalendarUtil.selectDate.plusMonths(1)
            setMonthview()
        }




        ///////////////////////////////////////////////////////////////


        addplan_button.setOnClickListener {
            loadData()
            val intent = Intent(activity, AddPlan_Activity::class.java)
            intent.apply {
                intent.putExtra("year", clicked_year.toString())//toString으로 형변환 해야 null값이 아닌채로 데이터 전송.0730
                intent.putExtra("month", clicked_month.toString())
                intent.putExtra("day", clicked_dayofMonth.toString())//dev브랜치테스트
                startActivity(intent)
            }


        }



    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onItemClick(dayText:LocalDate?){
        clicked_year = dayText?.year.toString()
        clicked_month = dayText?.monthValue.toString()
        clicked_dayofMonth = dayText?.dayOfMonth.toString()

        val db = Room.databaseBuilder(
            requireActivity().applicationContext, AppDatabase::class.java, "routine_databases"
        ).allowMainThreadQueries().build()

        //
        date_ = clicked_year+clicked_month+clicked_dayofMonth
        PlanList =
            db.plan_DAO().getPlanbyDate(date_).toTypedArray().toCollection(ArrayList<Plan>())
        println(PlanList)
        count = db.plan_DAO().countPlan(date_)
        listcount.text = count.toString()+"개"

        val Adapter = PlanAdapter(requireContext(), PlanList)
        listView.adapter = Adapter

        listView.onItemClickListener =
            AdapterView.OnItemClickListener { parent, view, position, id ->
                val selectItem = parent.getItemAtPosition(position) as Plan

                val intent = Intent(activity, ModPlan_Activity::class.java)
                intent.apply {
                    intent.putExtra(
                        "planid",
                        selectItem.id
                    )


                    startActivity(intent)

                }

            }
        db.close()

    }
    @RequiresApi(Build.VERSION_CODES.O)
    private fun setMonthview() {
        //년월 텍스트뷰 셋팅
        monthYearText_pl.text = monthYearFromDate(CalendarUtil.selectDate)

        //날짜 생성해서 리스트에 담기
        val dayList = dayInMonthArray(CalendarUtil.selectDate)

        //어댑터 초기화
        val adapter = Plan_CalendarAdapter(dayList, this)



        //레이아웃 설정(열 7개)
        var manager: RecyclerView.LayoutManager = GridLayoutManager(context?.applicationContext, 7)

        //레이아웃 적용
        recyclerview_pl.layoutManager = manager

        //어댑터 적용
        recyclerview_pl.adapter = adapter

    }
    @RequiresApi(Build.VERSION_CODES.O)
    private fun monthYearFromDate(date:LocalDate): String{

        var formatter = DateTimeFormatter.ofPattern("yyyy년 MM월")

        //받아온 날짜를를 해당 포맷으로 변경
        return date.format(formatter)
    }

    //날짜 타입(년,월)
    @RequiresApi(Build.VERSION_CODES.O)
    private fun yearMonthFromDate(date:LocalDate): String{

        var formatter = DateTimeFormatter.ofPattern("MM월 yyyy년")

        //받아온 날짜를를 해당 포맷으로 변경
        return date.format(formatter)
    }

    //날짜 생성
    @RequiresApi(Build.VERSION_CODES.O)
    private fun dayInMonthArray(date: LocalDate): ArrayList<LocalDate?> {

        var dayList = ArrayList<LocalDate?>()

        var yearMonth = YearMonth.from(date)

        //해당 월 마지막 날짜 가져오기
        var lastDay = yearMonth.lengthOfMonth()

        //해당 월의 첫번째 날짜 가져오기
        var firstDay = CalendarUtil.selectDate.withDayOfMonth(1)

        //첫번째 날 요일 가져오기기
        var dayOfWeek = firstDay.dayOfWeek.value

        for(i in 1..41){
            if(i <= dayOfWeek || i > (lastDay +dayOfWeek)){
                dayList.add(null)
            }else{
                //localDate.of(년,월,일)
                dayList.add(LocalDate.of(CalendarUtil.selectDate.year,
                    CalendarUtil.selectDate.monthValue,i - dayOfWeek))
            }
        }
        return dayList
    }
    private fun loadData(){
        clicked_year = pref_plan.getString("key_year",clicked_year).toString()
        clicked_month = pref_plan.getString("key_month",clicked_month).toString()
        clicked_dayofMonth = pref_plan.getString("key_day",clicked_dayofMonth).toString()

    }
}
