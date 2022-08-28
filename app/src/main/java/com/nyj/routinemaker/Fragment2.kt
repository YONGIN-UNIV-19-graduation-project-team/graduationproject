package com.nyj.routinemaker

import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
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
    //데이터바인딩, 뷰바인딩 사용하여 어댑터와 실시간 바인딩 가능
    private lateinit var binding: Fragment2Binding

    //전체 할일 더미 객체리스트 생성
    var PlanList = arrayListOf<Plan>(
        Plan(
            0L, "더미", "2022", "11",
            "12", "0", "0", ""
        )
    )

    //날짜, 클릭한 년,월,일, 개수 변수 초기화
    var date_ = ""
    var clicked_year = "2022"
    var clicked_month = "8"
    var clicked_dayofMonth = "20"
    var count = 0


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //뷰바인딩
        binding = Fragment2Binding.inflate(inflater, container, false)

        //오늘의 년,월,일을 변수에 할당. (기본값을 오늘로)
        clicked_year= LocalDate.now().year.toString()
        clicked_month= LocalDate.now().monthValue.toString()
        clicked_dayofMonth= LocalDate.now().dayOfMonth.toString()

        //쿼리문을 쉽게 사용하기 위해 년,월,일 String을 합쳤음
        date_ = clicked_year+clicked_month+clicked_dayofMonth

        //db연결
        val db = Room.databaseBuilder(
            requireActivity().applicationContext, AppDatabase::class.java, "routine_databases"
        ).allowMainThreadQueries().build()

        //해당하는 날짜의 플랜리스트를 db에서 가져왔다
        PlanList = db.plan_DAO().getPlanbyDate(date_).toTypedArray().toCollection(ArrayList<Plan>())

        //해당하는 날짜의 할 일의 총 개수
        count = db.plan_DAO().countPlan(date_)

        //db닫기
        db.close()


        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //pref_plan = requireActivity().applicationContext.getSharedPreferences("plan",0)


        //해당하는 날짜의 할일 개수 띄우기
        listcount.text = count.toString()+"개"

        //어댑터설정(할일 ListView)
        val Adapter = PlanAdapter(requireContext(), PlanList)
        listView.adapter = Adapter

        //ListView 클릭리스너 설정. 클릭시 인텐트로 id값 전달하고 할일수정으로 넘어감
        listView.onItemClickListener =
            AdapterView.OnItemClickListener { parent, view, position, id ->
                val selectItem = parent.getItemAtPosition(position) as Plan

                val intent = Intent(activity, ModPlan_Activity::class.java)
                intent.apply {
                    intent.putExtra("planid", selectItem.id)
                    startActivity(intent)
                }

            }

        //커스텀 달력을 뷰에 띄운다
        setMonthview()

        //이전달 버튼 이벤트
        pre_btn_pl.setOnClickListener {
            CalendarUtil.selectDate = CalendarUtil.selectDate.minusMonths(1)
            setMonthview()
        }

        //다음달 버튼 이벤트
        next_btn_pl.setOnClickListener {
            CalendarUtil.selectDate = CalendarUtil.selectDate.plusMonths(1)
            setMonthview()
        }

        //할일 추가 버튼 리스너
        addplan_button.setOnClickListener {

            val intent = Intent(activity, AddPlan_Activity::class.java)
            intent.apply {
                intent.putExtra("year", clicked_year.toString())//toString으로 형변환 해야 null값이 아닌채로 데이터 전송.0730
                intent.putExtra("month", clicked_month.toString())
                intent.putExtra("day", clicked_dayofMonth.toString())//dev브랜치테스트
                startActivity(intent)
            }
        }
    }

    //어댑터의 리사이클러뷰 클릭리스너 재정의. 인터페이스를 사용해서 재정의해주었다.
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onItemClick(dayText:LocalDate?){

        //클릭한 년,월,일값을 변수에 저장한다.
        clicked_year = dayText?.year.toString()
        clicked_month = dayText?.monthValue.toString()
        clicked_dayofMonth = dayText?.dayOfMonth.toString()

        //db연결
        val db = Room.databaseBuilder(
            requireActivity().applicationContext, AppDatabase::class.java, "routine_databases"
        ).allowMainThreadQueries().build()

        //쿼리문을 쉽게쓰려고 년,월,일 String 합침
        date_ = clicked_year+clicked_month+clicked_dayofMonth

        //클릭한 날짜의 할일들을 플랜리스트 객체리스트에 저장
        PlanList = db.plan_DAO().getPlanbyDate(date_).toTypedArray().toCollection(ArrayList<Plan>())

        //클릭한 날짜의 할일들 개수저장
        count = db.plan_DAO().countPlan(date_)

        //개수띄우기
        listcount.text = count.toString()+"개"

        //할일 리스트뷰 구현부
        val Adapter = PlanAdapter(requireContext(), PlanList)
        listView.adapter = Adapter

        //할일 리스트뷰 클릭리스너. (다시 구현하는 이유는 리스트뷰의 초기값(오늘)/클릭후 변화된 값 차이임)
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
    fun setMonthview() {
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

}
