package com.opsc.workmate.data

import java.time.LocalDate
import java.time.LocalTime

class Entry(
    val username: String,
    val categoryName: String,
    val date: LocalDate,
    val startTime: LocalTime,
    val endTime: LocalTime,
    val image: String?
    ) {
}