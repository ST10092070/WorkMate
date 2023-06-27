package com.opsc.workmate.data

import android.graphics.Color
import java.time.Duration
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Locale

class Category {
    var UID: String = ""
    var name: String = ""
    var colour: Int? = Color.WHITE
    var imageData: String? = null

    constructor() {
        // Empty constructor required by Firebase
    }

    constructor(UID: String, name: String, colour: Int? = Color.WHITE, imageData: String?) {
        this.UID = UID
        this.name = name
        this.colour = colour
        this.imageData = imageData
    }


    companion object {
        fun getTotalHours(entries: List<Entry>): String {
            var totalMinutes = 0

            for (entry in entries) {
                val startTime = entry.startTime
                val endTime = entry.endTime
                val difference = calculateTimeDifference(startTime, endTime)
                totalMinutes += difference
            }

            val hours = totalMinutes / 60
            val minutes = totalMinutes % 60

            return String.format("%02d:%02d", hours, minutes)
        }

        private fun calculateTimeDifference(startTime: String, endTime: String): Int {
            val formatter = DateTimeFormatter.ofPattern("h:mm a", Locale.ENGLISH)
            val start = LocalTime.parse(startTime, formatter)
            val end = LocalTime.parse(endTime, formatter)

            val duration = Duration.between(start, end)
            val minutes = duration.toMinutes().toInt()

            return minutes
        }

    }
}