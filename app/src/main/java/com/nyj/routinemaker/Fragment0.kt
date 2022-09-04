package com.nyj.routinemaker

import android.app.Activity
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatDelegate
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import kotlinx.android.synthetic.main.fragment0.*
import kotlinx.android.synthetic.main.fragment1.*
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList

class Fragment0 : Fragment() {
    //오늘의 루틴 빈 객체리스트 생성
    var todays_RoutineList = arrayListOf<Routine>()

    //오늘의 할일 빈 객체리스트 생성
    var todays_PlanList = arrayListOf<Plan>()

    //챌린지 더미 객체리스트 생성
    var challengeList = arrayListOf<Challenge>(
        Challenge(0L,"2022","5","5",
            0.0)
    )

    //전체 루틴 더미 객체리스트 생성
    var RoutineList = arrayListOf<Routine>(
        Routine(0L,"더미","55","55",
            true,true,true,true,true,true,true,false)
    )

    //전체 플랜 더미 객체리스트 생성
    var PlanList = arrayListOf<Plan>(
        Plan(
            0L, "더미", "2022", "11",
            "12", "0", "0", ""
        )
    )

    var count_checked=0 //수행한 루틴의 개수
    var count_all=0     //전체 루틴의 개수
    var isExist=0       //루틴/할일이 존재하는지
    var progressPercent=0.00//프로그레스바의 퍼센트 변수


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment0,container,false)

        return view
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        //현재 년,월,일 변수생성 후 저장
        val year= LocalDate.now().year.toString()
        val month= LocalDate.now().monthValue.toString()
        val day= LocalDate.now().dayOfMonth.toString()

        //db연결
        val db = Room.databaseBuilder(
            requireActivity().applicationContext,AppDatabase::class.java,"routine_databases"
        ).allowMainThreadQueries().build()

        //모든 루틴리스트,플랜리스트를 아까 생성한 객체리스트에 저장
        RoutineList = db.routine_DAO().getAll().toTypedArray().toCollection(ArrayList<Routine>())
        PlanList = db.plan_DAO().getAll().toTypedArray().toCollection(ArrayList<Plan>())

        //모든 루틴리스트를 람다식을 사용하여 오늘 수행해야 할 총 루틴 개수를 count_all 변수에 저장한다!
        RoutineList.forEach{routine ->

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

        //      (수행한 루틴의 개수/오늘 루틴의 개수)로 오늘 루틴을 몇퍼센트 수행했는지 percent 변수에 저장
        var percent=0.0
        count_checked = db.routine_DAO().howManyChecked()

        //여기서 에러가 많이 발생했다. 수행한 루틴의 개수가 0이면 null이 되었고 오늘 루틴의 개수가 0개면 infinity가 되었다.
        if(count_all==0||count_checked==0){
            //그래서 둘중 하나라도 0이면 그냥 0으로 지정하였다
            percent = 0.00
        }
        else {
            //계산
            percent = (count_checked.toDouble())/(count_all.toDouble())
        }

        //오늘의 날짜에 해당하는 챌린지가 존재하는지 변수에 저장
        isExist = db.challenge_DAO().isExist(year,month,day)

        //새로운 챌린지 객체를 생성(오늘자, 계산한 퍼센트)
        var challenge = Challenge(0L,year,month,day,percent)

        //오늘의 날짜에 해당하는 챌린지가 존재한다면
        if(isExist!=0){
            //오늘의 챌린지의 id를 가져와서 변수에 저장.
            val id = db.challenge_DAO().getId(year,month,day)
            //챌린지 객체 id를 오늘의 챌린지의 id로 변경
            challenge = Challenge(id,year,month,day,percent)
            //오늘 날짜의 챌린지를 새로 업데이트한다
            db.challenge_DAO().update(challenge)
        }
        //오늘의 날짜에 해당하는 챌린지가 존재하지 않는다면
        else {
            //오늘 날짜의 챌린지를 챌린지 db에 새로 추가한다
            db.challenge_DAO().insertAll(challenge)
        }

        //전체 챌린지 객체리스트를 db로 받아온다
        challengeList = db.challenge_DAO().getAll().toTypedArray().toCollection(arrayListOf<Challenge>())
//
//        //프로그레스바 퍼센트 계산(람다식)
//        challengeList.forEach { challenge ->
//            progressPercent+=(challenge.percent*3.4)
//        }
//
//        //프로그레스바 퍼센트 설정
//        progress_percent.text = progressPercent.toInt().toString()+"%"
//        //프로그레스바 상태
//        progressBar.progress = progressPercent.toInt()

        //현재 날짜
        CalendarUtil.selectDate = LocalDate.now()

        //커스텀캘린더 화면 뷰에 띄우기
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

        //루틴리스트에서 오늘 요일이 포함한 것들만 투데이루틴리스트에 저장
        RoutineList.forEach { routine ->
            val exroutine = Routine(routine.id,routine.name,routine.hour,routine.min,routine.mon,routine.tue,routine.wed,routine.thu,routine.fri,routine.sat,routine.sun,routine.routineischecked)
            var weekList = arrayListOf<Boolean>(routine.mon,routine.tue,routine.wed,routine.thu,routine.fri,routine.sat,routine.sun)

            if((doDayOfWeek()=="월"&&weekList[0])
                ||(doDayOfWeek()=="화"&&weekList[1])
                ||(doDayOfWeek()=="수"&&weekList[2])
                ||(doDayOfWeek()=="목"&&weekList[3])
                ||(doDayOfWeek()=="금"&&weekList[4])
                ||(doDayOfWeek()=="토"&&weekList[5])
                ||(doDayOfWeek()=="일"&&weekList[6])){
                todays_RoutineList.add(exroutine)
            }
        }

        //오늘의 요일 개수
        todaysroutinecount.text = todays_RoutineList.size.toString()+"개"

        //플랜리스트에서 오늘 날짜를 포함한 것들만 투데이플랜리스트에 저장
        val date = year+month+day
        //쿼리문 쉽게 쓰려고 년,월,일을 합친 date 변수를 생성했음
        todays_PlanList = db.plan_DAO().searchday(date).toTypedArray().toCollection(ArrayList<Plan>())

        //오늘의 할일 개수
        todaysplancount.text = todays_PlanList.size.toString()+"개"

        //db닫기
        db.close()


        //리스트뷰1(오늘의 루틴)

        val Adapter_rt = TodaysRoutineAdapter(requireContext(), todays_RoutineList)
        routineListview.adapter = Adapter_rt


        //리스트뷰2(오늘의 할일)

        val Adapter_pl = TodaysPlanAdapter(requireContext(), todays_PlanList)
        planListview.adapter = Adapter_pl


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

        //프로그레스바 계산
        //db연결
        val db = Room.databaseBuilder(
            requireActivity().applicationContext,AppDatabase::class.java,"routine_databases"
        ).allowMainThreadQueries().build()

        //이번달의 챌린지리스트를가져온다. 수정해야함
        challengeList = db.challenge_DAO().getChallengeFromYearAndMonth(CalendarUtil.selectDate.year.toString(),CalendarUtil.selectDate.monthValue.toString()).toTypedArray().toCollection(arrayListOf<Challenge>())
        println("@@@@@@@@@@@@@@@@"+challengeList)
        //프로그레스바 퍼센트 계산(람다식)
        progressPercent=0.0
        challengeList.forEach { challenge ->
            progressPercent+=(challenge.percent*3.4)
        }

        //프로그레스바 텍스트 띄우기
        if(progressPercent.toInt()<=100){
            progress_percent.text = progressPercent.toInt().toString()+"%"
        }else progress_percent.text = "100%"


        //프로그레스바 상태
        progressBar.progress = progressPercent.toInt()

        db.close()
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

    //오늘의 요일 구하기
    private fun doDayOfWeek(): String? {
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
