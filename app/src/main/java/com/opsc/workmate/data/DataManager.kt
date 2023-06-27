package com.opsc.workmate.data

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

object DataManager {

    //Variables
    private const val CATEGORIES_COLLECTION = "Categories" // Collection name in the Firebase database

    fun getCategories(uid: String): MutableList<Category> {
        val categories = mutableListOf<Category>()

        val database = FirebaseDatabase.getInstance()
        val categoryRef = database.getReference(CATEGORIES_COLLECTION)

        categoryRef.orderByChild("UID").equalTo(uid)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    for (snapshot in dataSnapshot.children) {
                        val category = snapshot.getValue(Category::class.java)
                        category?.let {
                            categories.add(it)
                        }
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    // Handle the error
                }
            })

        return categories
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
                    val exc = exception
                    callback(false) // Invoke the failure callback
                }
        } else {
            callback(false) // Invoke the failure callback if categoryId is null
        }
    }



}