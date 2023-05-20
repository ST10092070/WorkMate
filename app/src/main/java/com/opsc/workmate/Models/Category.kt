package com.opsc.workmate.Models

import kotlin.random.Random

data class Category(val CatID: String = getAutoId() ,val UserID: Int ,val Name: String = "" ,val Color: Int ,val Image: Int){
    companion object {
        fun getAutoId(): String {
            val random = Random(1000)
            val random1 = Random(100)
            return random1.nextInt(150).toString() + "cat" + random.nextInt(2000)
        }
    }
}
