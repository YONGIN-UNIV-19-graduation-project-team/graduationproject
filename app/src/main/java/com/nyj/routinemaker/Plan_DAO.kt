package com.nyj.routinemaker

import androidx.room.*

@Dao
interface Plan_DAO{
    @Query("SELECT *FROM tb_plans")
    fun getAll():List<Plan>

    @Insert
    fun insertAll(vararg plan: Plan)
    @Update
    fun update(vararg plan: Plan)
    @Delete
    fun delete(plan: Plan)

}