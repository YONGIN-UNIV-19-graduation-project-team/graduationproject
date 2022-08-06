package com.nyj.routinemaker

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.widget.RemoteViews

class WidgetProvider :AppWidgetProvider(){
    private val MY_ACTION = "android.action.MY_ACTION"

    private fun addviews(context: Context?):RemoteViews{
        val views = RemoteViews(context?.packageName,R.layout.widget)
        return views
    }

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
