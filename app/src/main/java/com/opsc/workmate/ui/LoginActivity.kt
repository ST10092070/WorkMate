package com.opsc.workmate.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.opsc.workmate.R
import com.opsc.workmate.data.Category
import com.opsc.workmate.data.Global
import com.opsc.workmate.data.User

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        insertSampleData()

    }

    private fun insertSampleData() {
        // Create a test user and add to the Global list
        val testUser = User("user", "1234", "test@example.com", "Test User")
        Global.users.add(testUser)

        val category = Category("user", "Studies", 1, null)
    }
}