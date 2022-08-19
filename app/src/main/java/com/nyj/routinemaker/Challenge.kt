package com.nyj.routinemaker

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tb_challenge")
data class Challenge(
    @PrimaryKey(autoGenerate = true) val id: Long,
    var year:String,
    var month:String,
    var day:String,

    var percent: Double,



    )