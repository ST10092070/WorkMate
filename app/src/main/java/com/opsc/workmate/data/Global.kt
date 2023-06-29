package com.opsc.workmate.data

//Singleton class for storing live data
object Global {
    var users: MutableList<User> = mutableListOf()
    var categories: MutableList<Category> = mutableListOf()
    var entries: MutableList<Entry> = mutableListOf()
    lateinit var goal: Goal

    var currentUser: User? = null
}