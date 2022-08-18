package com.nyj.routinemaker

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nyj.routinemaker.databinding.ActivityChallengeBinding
import kotlinx.android.synthetic.main.activity_challenge.*
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList

class Challenge_Activity : AppCompatActivity(), OnItemListener {

    private lateinit var binding: ActivityChallengeBinding

    //년월 변수
    private lateinit var selectedDate: LocalDate

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_challenge)

        //binding 초기화
        binding = DataBindingUtil.setContentView(this,R.layout.activity_challenge)

        //현재 날짜
        selectedDate = LocalDate.now()

        //화면 설정
        setMonthview()

        //이전달 버튼 이벤트
        binding.preBtn.setOnClickListener {
            selectedDate = selectedDate.minusMonths(1)
            setMonthview()
        }

        //다음달 버튼 이벤트
        binding.nextBtn.setOnClickListener {
            selectedDate = selectedDate.plusMonths(1)
            setMonthview()
        }
    }

    //날짜 화면에 보여주기
    @RequiresApi(Build.VERSION_CODES.O)
    private fun setMonthview() {
        //년월 텍스트뷰 셋팅
        binding.monthYearText.text = monthYearFromDate(selectedDate)

        //날짜 생성해서 리스트에 담기
        val dayList = dayInMonthArray(selectedDate)

        //어댑터 초기화
        val adapter = CalendarAdapter(dayList,this)

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
    private fun dayInMonthArray(date: LocalDate): ArrayList<String> {

        var dayList = ArrayList<String>()

        var yearMonth = YearMonth.from(date)

        //해당 월 마지막 날짜 가져오기
        var lastDay = yearMonth.lengthOfMonth()

        //해당 월의 첫번째 날짜 가져오기
        var firstDay = selectedDate.withDayOfMonth(1)

        //첫번째 날 요일 가져오기기
        var dayOfWeek = firstDay.dayOfWeek.value

        for(i in 1..41){
            if(i <= dayOfWeek || i > (lastDay +dayOfWeek)){
                dayList.add("")
            }else{
                dayList.add((i - dayOfWeek).toString())
            }
        }
        return dayList
    }

    //아이템 클릭 이벤트
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onItemClick(dayText: String) {
        var yearMonthDay = yearMonthFromDate(selectedDate) + " "+dayText + "일"

        Toast.makeText(this, yearMonthDay,Toast.LENGTH_SHORT).show()
    }
}