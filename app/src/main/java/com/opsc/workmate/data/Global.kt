package com.opsc.workmate.data

//Singleton class for storing live data
object Global {
    val users: MutableList<User> = mutableListOf()
    val categories: MutableList<Category> = mutableListOf()
    val entries: MutableList<Entry> = mutableListOf()

    var currentUser: User? = null
}