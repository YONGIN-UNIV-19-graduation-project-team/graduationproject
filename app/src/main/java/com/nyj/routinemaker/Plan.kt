package com.nyj.routinemaker

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tb_plans")
data class Plan(
    @PrimaryKey
    val name: String,

    var year:String,
    var month:String,
    var day:String,

    var hour: String,
    var min:String,



)