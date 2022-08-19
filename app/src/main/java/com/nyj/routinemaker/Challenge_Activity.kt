package com.nyj.routinemaker

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.LocaleList
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.nyj.routinemaker.databinding.ActivityChallengeBinding
import kotlinx.android.synthetic.main.activity_challenge.*
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList

class Challenge_Activity : AppCompatActivity() {

    var RoutineList = arrayListOf<Routine>(
        Routine(0L,"더미","55","55",
            true,true,true,true,true,true,true,false)
    )
    var count_checked=0
    var count_all=0
    var isExist=0
    private lateinit var binding: ActivityChallengeBinding

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_challenge)
        ///////////////////////////////////////////
        val year=LocalDate.now().year.toString()
        val month=LocalDate.now().monthValue.toString()
        val day=LocalDate.now().dayOfMonth.toString()

        val db = Room.databaseBuilder(
            applicationContext,AppDatabase::class.java,"routine_databases"
        ).allowMainThreadQueries().build()

        RoutineList = db.routine_DAO().getAll().toTypedArray().toCollection(ArrayList<Routine>())
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
        count_checked = db.routine_DAO().howManyChecked()
        val percent = (count_checked.toFloat())/(count_all.toFloat())
        println("@@@@@@@@@@@@@@@@@"+percent)
        isExist = db.challenge_DAO().isExist(year,month,day)
        val challenge = Challenge(0L,year,month,day,percent)
        val id = db.challenge_DAO().getId(year,month,day)
        if(isExist!=0){
            val challenge = Challenge(id,year,month,day,percent)
            db.challenge_DAO().update(challenge)
        }else db.challenge_DAO().insertAll(challenge)










        ///////////////////////////////////////////
        //binding 초기화
        binding = DataBindingUtil.setContentView(this,R.layout.activity_challenge)

        //현재 날짜
        CalendarUtil.selectDate = LocalDate.now()

        //화면 설정
        setMonthview()

        //이전달 버튼 이벤트
        binding.preBtn.setOnClickListener {
            CalendarUtil.selectDate = CalendarUtil.selectDate.minusMonths(1)
            setMonthview()
        }

        //다음달 버튼 이벤트
        binding.nextBtn.setOnClickListener {
            CalendarUtil.selectDate = CalendarUtil.selectDate.plusMonths(1)
            setMonthview()
        }
    }

    //날짜 화면에 보여주기
    @RequiresApi(Build.VERSION_CODES.O)
    private fun setMonthview() {
        //년월 텍스트뷰 셋팅
        binding.monthYearText.text = monthYearFromDate(CalendarUtil.selectDate)

        //날짜 생성해서 리스트에 담기
        val dayList = dayInMonthArray(CalendarUtil.selectDate)

        //어댑터 초기화
        val adapter = CalendarAdapter(dayList)

        //레이아웃 설정(열 7개)
        var manager:RecyclerView.LayoutManager = GridLayoutManager(applicationContext, 7)

        //레이아웃 적용
        binding.recyclerview.layoutManager = manager

        //어댑터 적용
        binding.recyclerview.adapter = adapter
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