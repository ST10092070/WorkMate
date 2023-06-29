package com.opsc.workmate.data

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.opsc.workmate.data.Global.categories

object DataManager {

    //Variables
    // Collection names in the Firebase database
    private const val CATEGORIES_COLLECTION = "Categories"
    private const val ENTRIES_COLLECTION = "Entries"

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

    fun getGoal(uid: String, callback: (Goal) -> Unit) {

    }

    fun setGoal(goal: Goal, callback: (Boolean) -> Unit) {

    }


}