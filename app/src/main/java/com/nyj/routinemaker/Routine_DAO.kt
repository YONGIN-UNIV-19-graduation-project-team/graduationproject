package com.nyj.routinemaker

import androidx.room.*

@Dao
interface Routine_DAO {
    @Query("SELECT *FROM tb_routines")
    fun getAll():List<Routine>

    @Insert
    fun insertAll(vararg routine: Routine)

    @Update
    fun update(vararg routine: Routine)
    @Delete
    fun delete(routine: Routine)
}