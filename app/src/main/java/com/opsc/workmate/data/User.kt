package com.opsc.workmate.data

class User {
    var uid: String? = null
    var username: String = ""
    var password: String = ""
    var email: String = ""
    var name: String = ""
    var workcoins: Int? = null

    constructor() {
        // Default constructor required by Firebase
    }

    constructor(
        uid: String?,
        username: String,
        password: String,
        email: String,
        name: String,
        workcoins: Int?
    ) {
        this.uid = uid
        this.username = username
        this.password = password
        this.email = email
        this.name = name
        this.workcoins = workcoins
    }
}

