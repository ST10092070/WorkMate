package com.opsc.workmate.Models

import kotlin.random.Random

data class User(val UserID: String = getAutoId(), val fullName: String = "", val userName: String = "", val email: String = "", var password: String = "",
val salt: String = "") {
    companion object {
        fun getAutoId(): String {
            val random = Random(1)
            val random1 = Random(1)
            return random1.nextInt(50).toString() + "user" + random.nextInt(1000)
        }
    }
}
