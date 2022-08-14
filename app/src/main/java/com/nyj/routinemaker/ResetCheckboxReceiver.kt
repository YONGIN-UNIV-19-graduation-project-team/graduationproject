package com.nyj.routinemaker

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.room.Room

class ResetCheckboxReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        //Room DB연동
        val db = Room.databaseBuilder(
            context.applicationContext,AppDatabase::class.java,"databases"
        ).allowMainThreadQueries().build()
        //체크박스를 모두 해제하는 쿼리문 호출
        db.routine_DAO().resetcheckbox()
        db.close()
    }
}