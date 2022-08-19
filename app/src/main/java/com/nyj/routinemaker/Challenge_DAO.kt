package com.nyj.routinemaker

import androidx.room.*

@Dao
interface Challenge_DAO {
    @Query("SELECT *FROM tb_challenge")
    fun getAll(): List<Challenge>

    @Insert
    fun insertAll(vararg challenge: Challenge)

    @Update
    fun update(vararg challenge: Challenge)

    @Delete
    fun delete(challenge: Challenge)

    @Query("SELECT percent FROM tb_challenge where year = :parameter1 AND month = :parameter2 AND day = :parameter3")
    fun getPercent(parameter1:String,parameter2:String,parameter3:String):Float

    @Query("SELECT count(*) FROM tb_challenge where year = :parameter4 AND month = :parameter5 AND day = :parameter6")
    fun isExist(parameter4:String,parameter5:String,parameter6:String):Int

    @Query("SELECT id FROM tb_challenge where year = :parameter7 AND month = :parameter8 AND day = :parameter9")
    fun getId(parameter7:String,parameter8:String,parameter9:String):Long
}