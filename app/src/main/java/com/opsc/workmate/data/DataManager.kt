package com.opsc.workmate.data

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

object DataManager {

    //Variables
    private const val CATEGORIES_COLLECTION = "Categories" // Collection name in the Firebase database

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



}