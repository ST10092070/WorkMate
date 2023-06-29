package com.opsc.workmate.data

import java.time.LocalTime

class Goal(
    val uid: String,
    val minGoal: LocalTime,
    val maxGoal: LocalTime
)