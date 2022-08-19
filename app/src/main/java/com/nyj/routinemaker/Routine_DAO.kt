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

    @Query("UPDATE tb_routines SET routineischecked=0 WHERE routineischecked=1")
    fun resetCheckBox()

    @Query("SELECT COUNT(*) FROM tb_routines")
    fun getCount():Int

    @Query("SELECT COUNT(*) FROM tb_routines where id LIKE :ID")
    fun getIdExist(ID:Long):Int

    @Query("SELECT COUNT(*) FROM tb_routines where routineischecked=1")
    fun howManyChecked():Int
}