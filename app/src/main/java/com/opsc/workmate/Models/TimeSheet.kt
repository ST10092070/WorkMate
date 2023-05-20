package com.opsc.workmate.Models

import java.sql.Date
import java.sql.Time
import kotlin.random.Random

data class TimeSheet(val EntryID: String = getAutoId(), val CatID: Int, val UserID: Int, val Date: Date, val StartTime: Time, val EndTime: Time, val Description: String = "", val Image: Int){
    companion object {
            fun getAutoId(): String {
            val random = Random(2000)
            val random1 = Random(50)
            return random1.nextInt(100).toString() + "sheet" + random.nextInt(3000)
        }
    }
}
