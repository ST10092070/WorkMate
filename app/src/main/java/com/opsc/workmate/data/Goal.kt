package com.opsc.workmate.data

class Goal() {
    lateinit var uid: String
    //Store time as string for firebase, doesn't support LocalTime
    lateinit var minGoal: String
    lateinit var maxGoal: String

    constructor(uid: String, minGoal: String, maxGoal: String) : this() {
        this.uid = uid
        this.minGoal = minGoal
        this.maxGoal = maxGoal
    }
}
