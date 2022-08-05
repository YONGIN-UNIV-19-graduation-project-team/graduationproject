package com.nyj.routinemaker

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(version = 1, entities = arrayOf(Routine::class,Plan::class), exportSchema = false)//entity가 여러개일 경우 arrayof로 묶는다
abstract class AppDatabase:RoomDatabase() {
    abstract fun routine_DAO():Routine_DAO
    abstract fun plan_DAO():Plan_DAO

}