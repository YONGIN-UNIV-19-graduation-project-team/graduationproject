package com.nyj.routinemaker

import java.time.LocalDate

interface OnItemListener {
    fun onItemClick(dayText:LocalDate?){
        println("?")
    }
}