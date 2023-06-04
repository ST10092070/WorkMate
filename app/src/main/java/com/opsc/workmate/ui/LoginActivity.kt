package com.opsc.workmate.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.opsc.workmate.R
import com.opsc.workmate.data.Category
import com.opsc.workmate.data.Entry
import com.opsc.workmate.data.Global
import com.opsc.workmate.data.User

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        insertSampleData()

    }

    private fun insertSampleData() {
        val testUser = User("user", "1234", "test@example.com", "Test User")
        Global.users.add(testUser)

        var category = Category("user", "Studies", -9381821, null)
        Global.categories.add(category)
        category = Category("user", "Work", -9381421, null)
        Global.categories.add(category)

        var entry = Entry("user", "Studies", "12/12/2023", "12:10 am", "12:20 am", "null")
        Global.entries.add(entry)
        entry = Entry("user", "Work", "12/12/2023", "12:10 am", "12:20 am", "null")
        Global.entries.add(entry)
    }
}