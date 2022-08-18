package com.nyj.routinemaker

import androidx.room.*

@Dao
interface Plan_DAO{
    @Query("SELECT *FROM tb_plans")
    fun getAll():List<Plan>
    @Query("SELECT *FROM tb_plans WHERE id LIKE :searchQuery")
    fun getPlanbyId(searchQuery: Long) : Plan
    @Query("SELECT *FROM tb_plans WHERE date LIKE :date")
    fun getPlanbyDate(date:String):List<Plan>
    @Insert
    fun insertAll(vararg plan: Plan)
    @Update
    fun update(vararg plan: Plan)
    @Delete
    fun delete(plan: Plan)
    @Query("SELECT *FROM tb_plans WHERE day LIKE :searchQuery2")
    fun searchday(searchQuery2: String) : List<Plan>
    @Query("SELECT COUNT(*) FROM tb_plans where date LIKE :date")
    fun countPlan(date: String):Int
}