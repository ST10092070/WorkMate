package com.opsc.workmate.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.opsc.workmate.R
import com.opsc.workmate.data.Category
import com.opsc.workmate.data.Entry
import com.opsc.workmate.data.Global
import com.opsc.workmate.data.User
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.Random

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        insertSampleData()

    }

    private fun insertSampleData() {
        //Method to insert sample data to use for testing
        //WARNING: data is randomly generated so can be weird sometimes
        val testUser = User("user", "1234", "test@example.com", "Test User")
        Global.users.add(testUser)

        var category = Category("user", "Studies", -9381821, "null")
        Global.categories.add(category)
        category = Category("user", "Work", -9381421, "null")
        Global.categories.add(category)
        category = Category("user", "Fitness", -16579837, "null")
        Global.categories.add(category)
        category = Category("user", "Hobbies", -5874169, "null")
        Global.categories.add(category)

        var entry = Entry("user", "Studies", "12/06/2023", "12:10 AM", "12:20 AM", "null", "example")
        Global.entries.add(entry)


        val random = Random()

        val categoryNames = listOf("Studies", "Work", "Fitness", "Hobbies")

        val calendar = Calendar.getInstance()
        val currentMonth = calendar.get(Calendar.MONTH)
        val currentYear = calendar.get(Calendar.YEAR)
        calendar.set(currentYear, currentMonth, 1)
        val daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)

        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val timeFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())

        for (i in 0 until 20) {
            val categoryName = categoryNames[random.nextInt(categoryNames.size)]
            val dayOfMonth = random.nextInt(daysInMonth) + 1
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            val date = dateFormat.format(calendar.time)
            val startTime = timeFormat.format(calendar.time)

            val hourDifference = random.nextInt(3) + 1
            calendar.add(Calendar.HOUR_OF_DAY, hourDifference)
            val endTime = timeFormat.format(calendar.time)

            entry = Entry("user", categoryName, date, startTime, endTime, "null", "Description example")
            Global.entries.add(entry)
        }
    }

}