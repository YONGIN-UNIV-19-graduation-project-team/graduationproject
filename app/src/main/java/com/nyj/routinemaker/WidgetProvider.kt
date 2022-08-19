package com.nyj.routinemaker

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.os.Build
import android.widget.RemoteViews
import androidx.annotation.RequiresApi
import androidx.room.Room
import java.time.LocalDate
import java.util.*


class WidgetProvider :AppWidgetProvider(){
    private val MY_ACTION = "android.action.MY_ACTION"

    var year=""
    var month = ""
    var day=""
    var hour = ""
    var min = ""
    var date=""
    var Planlist = arrayListOf<Plan>(
        Plan(0L,"더미","2022","11",
            "12","0","0","0")
    )


    @RequiresApi(Build.VERSION_CODES.O)
    private fun addviews(context: Context?):RemoteViews{
        val views = RemoteViews(context?.packageName,R.layout.widget)
        val viewname = StringBuilder()


        //db연동, 안될수도있음
        val db = Room.databaseBuilder(
            context!!.applicationContext,AppDatabase::class.java,"routine_databases"
        ).allowMainThreadQueries().build()

        year=LocalDate.now().year.toString()
        month=LocalDate.now().monthValue.toString()
        day=LocalDate.now().dayOfMonth.toString()
        date=year+month+day
        Planlist = db.plan_DAO().searchday(day).toTypedArray().toCollection(arrayListOf<Plan>())

        Planlist.forEach{Plan->
            viewname.append(Plan.name).append(" ")

        }
        db.close()
        views.setTextViewText(R.id.todays_plan,viewname.toString())
        views.setTextViewText(R.id.day_of_month,day)
        views.setTextViewText(R.id.day_of_week,doDayOfWeek()+"요일")
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