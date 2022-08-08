package com.nyj.routinemaker

import androidx.room.*

@Dao
interface Routine_DAO {
    @Query("SELECT *FROM tb_routines")
    fun getAll():List<Routine>

    @Query("SELECT *FROM tb_routines WHERE id LIKE :searchQuery")
    fun getRoutinebyId(searchQuery: Long) : Routine

    @Insert
    fun insertAll(vararg routine: Routine)

    @Update
    fun update(vararg routine: Routine)

    @Delete
    fun delete(routine: Routine)

}