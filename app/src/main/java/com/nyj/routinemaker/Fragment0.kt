package com.nyj.routinemaker

import android.app.Activity
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import kotlinx.android.synthetic.main.fragment0.*
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList

class Fragment0 : Fragment() {

    var todays_RoutineList = arrayListOf<Routine>(
        Routine(0L,"더미","55","55",
            true,true,true,true,true,true,true,false)
    )
    var challengeList = arrayListOf<Challenge>(
        Challenge(0L,"2022","5","5",
            0.0)
    )
    var RoutineList = arrayListOf<Routine>(
        Routine(0L,"더미","55","55",
            true,true,true,true,true,true,true,false)
    )
    var PlanList = arrayListOf<Plan>(
        Plan(
            0L, "더미", "2022", "11",
            "12", "0", "0", ""
        )
    )
    var count_checked=0
    var count_all=0
    var isExist=0
    var progressPercent=0.00

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment0,container,false)
        /////////////db구현 후 리스트에 추가/////////////////



        ////////////////////////////////////////////////
        return view
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val year= LocalDate.now().year.toString()
        val month= LocalDate.now().monthValue.toString()
        val day= LocalDate.now().dayOfMonth.toString()

        val db = Room.databaseBuilder(
            requireActivity().applicationContext,AppDatabase::class.java,"routine_databases"
        ).allowMainThreadQueries().build()

        todays_RoutineList = db.routine_DAO().getAll().toTypedArray().toCollection(ArrayList<Routine>())
        todays_RoutineList.forEach{routine ->
            var weekList = arrayListOf<Boolean>(routine.mon,routine.tue,routine.wed,routine.thu,routine.fri,routine.sat,routine.sun)
            if((doDayOfWeek()=="월"&&weekList[0])
                ||(doDayOfWeek()=="화"&&weekList[1])
                ||(doDayOfWeek()=="수"&&weekList[2])
                ||(doDayOfWeek()=="목"&&weekList[3])
                ||(doDayOfWeek()=="금"&&weekList[4])
                ||(doDayOfWeek()=="토"&&weekList[5])
                ||(doDayOfWeek()=="일"&&weekList[6]))
            {
                count_all++
            }

        }
        count_checked = db.routine_DAO().howManyChecked()
        val percent = (count_checked.toDouble())/(count_all.toDouble())

        isExist = db.challenge_DAO().isExist(year,month,day)
        val challenge = Challenge(0L,year,month,day,percent)
        val id = db.challenge_DAO().getId(year,month,day)
        if(isExist!=0){
            val challenge = Challenge(id,year,month,day,percent)
            db.challenge_DAO().update(challenge)
        }else db.challenge_DAO().insertAll(challenge)

        challengeList = db.challenge_DAO().getAll().toTypedArray().toCollection(arrayListOf<Challenge>())
        println(challengeList)
        challengeList.forEach { challenge ->
            progressPercent+=(challenge.percent*3.4)
        }


        progress_percent.text = progressPercent.toInt().toString()
        println(progress_percent.text)
        progressBar.progress = progressPercent.toInt()
        println(progressPercent.toInt())
        db.close()
        //현재 날짜
        CalendarUtil.selectDate = LocalDate.now()

        //화면 설정
        setMonthview()

        //이전달 버튼 이벤트
        pre_btn.setOnClickListener {
            CalendarUtil.selectDate = CalendarUtil.selectDate.minusMonths(1)
            setMonthview()
        }

        //다음달 버튼 이벤트
        next_btn.setOnClickListener {
            CalendarUtil.selectDate = CalendarUtil.selectDate.plusMonths(1)
            setMonthview()
        }
        //////////////////////////////////////////////리스트뷰1(오늘의 루틴)







        //////////////////////////////////////////////리스트뷰2(오늘의 할일)






        ////////////////////////////////////////////////////////////////
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setMonthview() {
        //년월 텍스트뷰 셋팅
        monthYearText.text = monthYearFromDate(CalendarUtil.selectDate)

        //날짜 생성해서 리스트에 담기
        val dayList = dayInMonthArray(CalendarUtil.selectDate)

        //어댑터 초기화
        val adapter = CalendarAdapter(dayList)

        //레이아웃 설정(열 7개)
        var manager: RecyclerView.LayoutManager = GridLayoutManager(context?.applicationContext, 7)

        //레이아웃 적용
        recyclerview.layoutManager = manager

        //어댑터 적용
        recyclerview.adapter = adapter

    }
    //날짜 타입 설정(월,년)
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

    private fun doDayOfWeek(): String? {//오늘의 요일 구하기
        val cal: Calendar = Calendar.getInstance()
        var strWeek: String? = null
        val nWeek: Int = cal.get(Calendar.DAY_OF_WEEK)

        if (nWeek == 1) {
            strWeek = "일"
        } else if (nWeek == 2) {
            strWeek = "월"
        } else if (nWeek == 3) {
            strWeek = "화"
        } else if (nWeek == 4) {
            strWeek = "수"
        } else if (nWeek == 5) {
            strWeek = "목"
        } else if (nWeek == 6) {
            strWeek = "금"
        } else if (nWeek == 7) {
            strWeek = "토"
        }
        return strWeek
    }
}
