package com.opsc.workmate.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.navigation.Navigation
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.opsc.workmate.MainActivity
import com.opsc.workmate.R
import com.opsc.workmate.data.Category
import com.opsc.workmate.data.Entry
import com.opsc.workmate.data.Global
import com.opsc.workmate.data.User

class LoginFragment : Fragment() {
    private lateinit var auth: FirebaseAuth
    private var userDatabaseReference: DatabaseReference? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()

        userDatabaseReference = FirebaseDatabase.getInstance().getReference("User")

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_login, container, false)

        //---- Navigate to register screen ----
        // Find the TextView by ID
        val txtSignUp: TextView = view.findViewById(R.id.txtSignUp)

        // Set onClickListener for the TextView
        txtSignUp.setOnClickListener {
            // Get the NavController
            val navController = Navigation.findNavController(view)

            // Navigate to the registerFragment
            navController.navigate(R.id.action_loginFragment_to_registerFragment)
        }

        //---- Login button ----
        //Find the button by ID
        val btnLogin: Button = view.findViewById(R.id.btnLogin)

        // Set onClickListener for button
        btnLogin.setOnClickListener {
            // Perform login validation

            val txtUsername: EditText = requireView().findViewById(R.id.txtUsername)
            val txtPassword: EditText = requireView().findViewById(R.id.txtPassword)

            val email = txtUsername.text.toString()
            val password = txtPassword.text.toString()

            loginWithEmailPassword(email, password)
        }

        val imgIcon : ImageView = view.findViewById(R.id.imgIcon)
        val drawable = ContextCompat.getDrawable(requireContext(), R.drawable.workmate_logo)
        imgIcon.setImageDrawable(drawable)

        return view
    }

    private fun loginWithEmailPassword(email: String, password: String) {
        // Perform input validation
        if (email.isBlank() || password.isBlank()) {
            // Handle empty email or password
            Log.d("LoginActivity", "Empty email or password")
            return
        }

        // Get an instance of FirebaseAuth
        val auth = FirebaseAuth.getInstance()

        // Sign in with email and password
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Login successful
                    Log.d("LoginActivity", "Login successful")

                    val firebaseUser = auth.currentUser
                    val uid = firebaseUser?.uid

                    if (uid != null) {
                        userDatabaseReference?.child(uid)?.addListenerForSingleValueEvent(object : ValueEventListener {
                            override fun onDataChange(dataSnapshot: DataSnapshot) {
                                val userSnapshot = dataSnapshot.getValue(User::class.java)
                                if (userSnapshot != null) {
                                    Global.currentUser = userSnapshot

                                    //Navigate
                                    val intent = Intent(activity, MainActivity::class.java)
                                    startActivity(intent)
                                } else {
                                    // User data not found in the database
                                    Log.d("LoginActivity", "User data not found in the database")
                                    Toast.makeText(context, "User data not found in the database", Toast.LENGTH_SHORT).show()
                                }
                            }

                            override fun onCancelled(databaseError: DatabaseError) {
                                // Error occurred while accessing user data in the database
                                Log.d("LoginActivity", "Error accessing user data in the database: ${databaseError.message}")
                                Toast.makeText(context, "Error accessing user data in the database", Toast.LENGTH_SHORT).show()
                            }
                        })
                    }
                } else {
                    // Login failed
                    Log.d("LoginActivity", "Login failed: ${task.exception?.message}")

                    // Show message
                    Toast.makeText(context, "Authentication failed.", Toast.LENGTH_SHORT).show()
                }
            }

    }


    //TODO: Figure this out
    private fun filterLists() {
        //Filter global lists according to logged in user, other user's data is not needed
        //Other user's data gets repopulated each time app opens/LoginActivity

        //Code Attribution
        //The below code was derived from StackOverflow
        //https://stackoverflow.com/questions/70252668/filter-a-list-of-object
        //Erjon
        //https://stackoverflow.com/users/6310948/erjon

        val entries = Global.entries
        val filteredEntries: MutableList<Entry> = mutableListOf()
        entries.forEach { entry ->
            if (entry.UID.equals(Global.currentUser!!.username, ignoreCase = true))
                filteredEntries.add(entry) }
        Global.entries = filteredEntries

        val categories = Global.categories
        val filteredCategories: MutableList<Category> = mutableListOf()
        categories.forEach { category ->
            if (category.UID.equals(Global.currentUser!!.username, ignoreCase = true))
                filteredCategories.add(category) }
        Global.categories = filteredCategories
    }
}