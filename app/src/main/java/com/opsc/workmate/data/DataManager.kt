package com.opsc.workmate.data

import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

object DataManager {

    //Variables
    // Collection names in the Firebase database
    private const val CATEGORIES_COLLECTION = "Categories"
    private const val ENTRIES_COLLECTION = "Entries"
    private const val GOALS_COLLECTION = "Goals"
    private const val USERS_COLLECTION = "User"

    fun getCategories(uid: String, callback: (MutableList<Category>) -> Unit) {
        val categories = mutableListOf<Category>()

        val database = FirebaseDatabase.getInstance()
        val categoryRef = database.getReference(CATEGORIES_COLLECTION)

        // Query the categories based on the specified UID
        categoryRef.orderByChild("uid").equalTo(uid)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    // Iterate over the retrieved data snapshots
                    for (snapshot in dataSnapshot.children) {
                        // Retrieve the category object from the snapshot
                        val category = snapshot.getValue(Category::class.java)
                        category?.let {
                            // Add the category to the list
                            categories.add(it)
                        }
                    }
                    // Invoke the callback function with the retrieved categories
                    callback(categories)
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    // Handle the error
                    callback(mutableListOf()) // Pass an empty list in case of error
                }
            })
    }

    fun addCategory(category: Category, callback: (Boolean) -> Unit) {
        val database = FirebaseDatabase.getInstance()
        val categoryRef = database.getReference(CATEGORIES_COLLECTION)

        // Generate a random ID for the category entry
        val categoryId = categoryRef.push().key

        // Add the category to the Firebase database using the generated ID
        if (categoryId != null) {
            categoryRef.child(categoryId).setValue(category)
                .addOnSuccessListener {
                    // Category added successfully
                    callback(true) // Invoke the success callback
                }
                .addOnFailureListener { exception ->
                    // Error occurred while adding the category
                    //Do something with exception...
                    callback(false) // Invoke the failure callback
                }
        } else {
            callback(false) // Invoke the failure callback if categoryId is null
        }
    }

    fun getEntries(uid: String, callback: (MutableList<Entry>) -> Unit) {
        val entries = mutableListOf<Entry>()

        val database = FirebaseDatabase.getInstance()
        val entryRef = database.getReference(ENTRIES_COLLECTION)

        // Query the categories based on the specified UID
        entryRef.orderByChild("uid").equalTo(uid)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    // Iterate over the retrieved data snapshots
                    for (snapshot in dataSnapshot.children) {
                        // Retrieve the entry object from the snapshot
                        val entry = snapshot.getValue(Entry::class.java)
                        entry?.let {
                            // Add the entry to the list
                            entries.add(it)
                        }
                    }
                    // Invoke the callback function with the retrieved entries
                    callback(entries)
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    // Handle the error
                    callback(mutableListOf()) // Pass an empty list in case of error
                }
            })


    }

    fun addEntry(entry: Entry, callback: (Boolean) -> Unit) {
        val database = FirebaseDatabase.getInstance()
        val entryRef = database.getReference(ENTRIES_COLLECTION)

        // Generate a random ID for the category entry
        val entryId = entryRef.push().key

        // Add the category to the Firebase database using the generated ID
        if (entryId != null) {
            entryRef.child(entryId).setValue(entry)
                .addOnSuccessListener {
                    // Category added successfully
                    callback(true) // Invoke the success callback
                }
                .addOnFailureListener { exception ->
                    // Error occurred while adding the category
                    //Do something with exception...
                    callback(false) // Invoke the failure callback
                }
        } else {
            callback(false) // Invoke the failure callback if categoryId is null
        }
    }

    fun getGoal(uid: String, callback: (Goal?) -> Unit) {
        val database = FirebaseDatabase.getInstance()
        val goalRef = database.getReference(GOALS_COLLECTION)

        val query = goalRef.orderByChild("uid").equalTo(uid)
        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    val goalSnapshot = dataSnapshot.children.first()
                    val goal = goalSnapshot.getValue(Goal::class.java)
                    callback(goal)
                } else {
                    callback(null) // No goal found for the specified UID
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                //Something went wrong
                callback(null) // Pass null in case of error
            }
        })
    }


    fun setGoal(goal: Goal, callback: (Boolean) -> Unit) {
        val database = FirebaseDatabase.getInstance()
        val goalRef = database.getReference(GOALS_COLLECTION)

        //Search for already-existing entry for this user
        val query = goalRef.orderByChild("uid").equalTo(goal.uid)
        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    // User already has a goal entry, update it with the new goal
                    val goalSnapshot = dataSnapshot.children.first()
                    val goalId = goalSnapshot.key
                    goalId?.let {
                        goalRef.child(it).setValue(goal)
                            .addOnSuccessListener {
                                // Goal updated successfully
                                callback(true) // Invoke the success callback
                            }
                            .addOnFailureListener { exception ->
                                // Error occurred while updating the goal
                                callback(false) // Invoke the failure callback
                            }
                    }
                } else {
                    // No goal entry found, create a new one and set the goal object as its data
                    val goalId = goalRef.push().key
                    goalId?.let {
                        goalRef.child(it).setValue(goal)
                            .addOnSuccessListener {
                                // Goal added successfully
                                callback(true) // Invoke the success callback
                            }
                            .addOnFailureListener { exception ->
                                // Error occurred while adding the goal
                                callback(false) // Invoke the failure callback
                            }
                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                //Something went wrong
                callback(false) // Invoke the failure callback
            }
        })
    }

    fun setWorkcoins(workcoins: Int, callback: (Boolean) -> Unit) {
        val database = FirebaseDatabase.getInstance()
        val userRef = database.getReference(USERS_COLLECTION)

        val id = Global.currentUser!!.uid

        // Add the category to the Firebase database using the generated ID
        if (id != null) {
            val workcoinsRef = userRef.child(id).child("workcoins")
            workcoinsRef.setValue(workcoins)
                .addOnSuccessListener {
                    // Category added successfully
                    callback(true) // Invoke the success callback
                }
                .addOnFailureListener { exception ->
                    // Error occurred while adding the category
                    //Do something with exception...
                    callback(false) // Invoke the failure callback
                }
        } else {
            callback(false) // Invoke the failure callback if categoryId is null
        }
    }

    fun getWorkcoins(uid: String, callback: (Any) -> Unit) {

        val database = FirebaseDatabase.getInstance()
        val userRef = database.getReference(USERS_COLLECTION)

        val workcoinsRef = userRef.child(uid).child("workcoins")

        // Query the categories based on the specified UID
        workcoinsRef.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {

                    val workcoins = dataSnapshot.value

                    if(workcoins!=null){
                        callback(workcoins)
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    // Handle the error
                    callback(10) // Invoke the failure callback
                    // Failed to read the value
                    Log.d("Failed to read the value","Error: ${databaseError.message}")
                }
            })
    }

}