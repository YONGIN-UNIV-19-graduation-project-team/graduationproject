package com.nyj.routinemaker

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDate

class CalendarUtil {
    companion object{
        @RequiresApi(Build.VERSION_CODES.O)
        var selectDate: LocalDate = LocalDate.now()
    }
}