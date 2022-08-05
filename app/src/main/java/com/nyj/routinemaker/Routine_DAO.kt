package com.nyj.routinemaker

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface Routine_DAO {
    @Query("SELECT *FROM tb_routines")
    fun getAll():List<Routine>

    @Insert
    fun insertAll(vararg routine: Routine)

    @Delete
    fun delete(routine: Routine)
}