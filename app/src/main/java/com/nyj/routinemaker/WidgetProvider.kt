package com.nyj.routinemaker

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.os.Build
import android.widget.RemoteViews
import androidx.annotation.RequiresApi
import androidx.room.Room
import java.time.LocalDate


class WidgetProvider :AppWidgetProvider(){
    private val MY_ACTION = "android.action.MY_ACTION"

    var year=""
    var month = ""
    var day=""
    var hour = ""
    var min = ""

    var Planlist = arrayListOf<Plan>(
        Plan(0L,"더미","2022","11",
            "12","0","0")
    )


    @RequiresApi(Build.VERSION_CODES.O)
    private fun addviews(context: Context?):RemoteViews{
        val views = RemoteViews(context?.packageName,R.layout.widget)
        val viewname = StringBuilder()


        //db연동, 안될수도있음
        val db = Room.databaseBuilder(
            context!!.applicationContext,AppDatabase::class.java,"database"
        ).allowMainThreadQueries().build()

        year=LocalDate.now().year.toString()
        month=LocalDate.now().month.toString()
        day=LocalDate.now().dayOfMonth.toString()

        Planlist = db.plan_DAO().searchday(day).toTypedArray().toCollection(arrayListOf<Plan>())

        Planlist.forEach{Plan-> viewname.append(Plan.name).append(" ")}
        println(viewname)
        views.setTextViewText(R.id.todays_plan,viewname.toString())

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
}
