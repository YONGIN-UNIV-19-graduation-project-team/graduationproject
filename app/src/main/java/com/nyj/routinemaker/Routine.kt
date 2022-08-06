package com.nyj.routinemaker

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tb_routines")
data class Routine(
    @PrimaryKey(autoGenerate = true) val id: Long,
    val name:String,


    var hour: String,
    var min:String,

    var mon:Boolean,
    var tue:Boolean,
    var wed:Boolean,
    var thu:Boolean,
    var fri:Boolean,
    var sat:Boolean,
    var sun:Boolean

)
