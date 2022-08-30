package com.nyj.routinemaker

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Build
import android.widget.RemoteViews
import androidx.annotation.RequiresApi
import androidx.room.Room
import kotlinx.android.synthetic.main.widget.*
import kotlinx.android.synthetic.main.activity_main.*
import java.time.LocalDate
import java.util.*


class WidgetProvider :AppWidgetProvider(){
    private val MY_ACTION = "android.action.MY_ACTION"

    //년,월,일,시간 변수 초기화
    var year=""
    var month = ""
    var day=""
    var hour = ""
    var min = ""
    var date=""

    //할일 객체리스트 선언
    var Planlist = arrayListOf<Plan>(
        Plan(0L,"더미","2022","11",
            "12","0","0","0")
    )


    @RequiresApi(Build.VERSION_CODES.O)
    private fun addviews(context: Context?):RemoteViews{
        val views = RemoteViews(context?.packageName,R.layout.widget)

        //StringBuilder 객체 생성
        val viewname = StringBuilder()


        //db연동
        val db = Room.databaseBuilder(
            context!!.applicationContext,AppDatabase::class.java,"routine_databases"
        ).allowMainThreadQueries().build()

        //변수에 오늘자 년,월,일 저장
        year=LocalDate.now().year.toString()
        month=LocalDate.now().monthValue.toString()
        day=LocalDate.now().dayOfMonth.toString()

        //쿼리문 쉽게 쓰려고 년,월,일 String 합침
        date=year+month+day

        //쿼리문으로 해당하는 날짜 플랜들 PlanList에 넣기
        Planlist = db.plan_DAO().searchday(date).toTypedArray().toCollection(arrayListOf<Plan>())
        //혹시모르니출력해봄
        println(date+"date출력")

        //람다식으로 행 나누기
        Planlist.forEach{Plan-> viewname.append(Plan.name).append("\n- ") }

        //할일 객체리스트의 개수를 변수에 저장
        val plan_Count = Planlist.size.toString()

        //db종료
        db.close()

        //텍스트 뷰 설정
        views.setTextViewText(R.id.todays_plan,"- "+ viewname.toString())
        views.setTextViewText(R.id.day_of_month,day)
        views.setTextViewText(R.id.day_of_week,doDayOfWeek())
        views.setTextViewText(R.id.plancount,plan_Count)

        //위젯의 오늘의 할일 클릭 시 메인액티비티 실행
        views.setOnClickPendingIntent(R.id.todays_plan,buildStartIntent(context))


        return views
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onUpdate(
        context: Context?,
        appWidgetManager: AppWidgetManager?,
        appWidgetIds: IntArray?
    ) {
        super.onUpdate(context, appWidgetManager, appWidgetIds)

        appWidgetIds?.forEach { appWidgetId->
            val views:RemoteViews = addviews(context)
            appWidgetManager?.updateAppWidget(appWidgetId,views)
        }
    }

    private fun buildStartIntent(context: Context?):PendingIntent{
        var intent = Intent(Intent.ACTION_MAIN)
        intent.addCategory(Intent.CATEGORY_LAUNCHER)
        intent.setComponent(ComponentName(context!!.applicationContext,MainActivity::class.java))
        val pendingIntent = PendingIntent.getActivity(context,999,intent,0)
        return pendingIntent
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