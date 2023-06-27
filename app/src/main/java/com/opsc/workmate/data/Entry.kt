package com.opsc.workmate.data

class Entry() {
    lateinit var uid: String
    lateinit var categoryName: String
    lateinit var date: String
    lateinit var startTime: String
    lateinit var endTime: String
    var imageData: String? = null
    lateinit var description: String

    constructor(
        uid: String,
        categoryName: String,
        date: String,
        startTime: String,
        endTime: String,
        imageData: String?,
        description: String
    ) : this() {
        this.uid = uid
        this.categoryName = categoryName
        this.date = date
        this.startTime = startTime
        this.endTime = endTime
        this.imageData = imageData
        this.description = description
    }
}

