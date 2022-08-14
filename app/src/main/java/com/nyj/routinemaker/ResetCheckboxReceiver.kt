package com.nyj.routinemaker

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.room.Room

class ResetCheckboxReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        //여기에 체크박스 해제 코드 작성해야함
        val db = Room.databaseBuilder(
            context.applicationContext,AppDatabase::class.java,"databases"
        ).allowMainThreadQueries().build()
        db.routine_DAO().resetcheckbox()
        db.close()
    }
}