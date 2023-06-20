package com.opsc.workmate.ui

import android.content.ContentValues
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
import androidx.core.content.res.ResourcesCompat
import androidx.navigation.Navigation
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.Query
import com.google.firebase.database.ValueEventListener
import com.opsc.workmate.MainActivity
import com.opsc.workmate.R
import com.opsc.workmate.data.Category
import com.opsc.workmate.data.Entry
import com.opsc.workmate.data.Global
import java.util.logging.Logger.global

class LoginFragment : Fragment() {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()
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
            val isValid = validateLogin()

            if (isValid) {
                // Navigate to MainActivity
                val intent = Intent(activity, MainActivity::class.java)
                startActivity(intent)
            } else {
                // Display an error message or handle the invalid login case
                Toast.makeText(activity, "Invalid login credentials", Toast.LENGTH_SHORT).show()
            }
        }

        val imgIcon : ImageView = view.findViewById(R.id.imgIcon)
        val drawable = ContextCompat.getDrawable(requireContext(), R.drawable.workmate_logo)
        imgIcon.setImageDrawable(drawable)

        return view
    }

    private fun validateLogin(): Boolean {
        val txtUsername: EditText = requireView().findViewById(R.id.txtUsername)
        val txtPassword: EditText = requireView().findViewById(R.id.txtPassword)

        val username = txtUsername.text.toString()
        val password = txtPassword.text.toString()

        // Perform input validation
        if (username.equals("") || password.equals("")){
            Toast.makeText(context, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            return false
        }
        //get the user reference in firebase
        val reference = FirebaseDatabase.getInstance().getReference("User")
        //check if there is any user with the entered user name

        val checkUser: Query = reference.orderByChild("name")
            .equalTo(username)

        checkUser.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    val fb_name = snapshot.child(username).child("name").getValue(String::class.java)
                    val fb_email = snapshot.child(username).child("email").getValue(String::class.java)
                    val fb_password = snapshot.child(username).child("password").getValue(String::class.java)

                    //check if the user doesn't exists
                    if(!username.equals(fb_name) || !password.equals(fb_password)){
                        Toast.makeText(
                            context,
                            "user doesn't exist!",
                            Toast.LENGTH_SHORT,
                        ).show()
                    }else{
                        if (fb_email != null) {
                            //try to sign the user in
                            try{
                                this@LoginFragment.auth.signInWithEmailAndPassword(fb_email, fb_password)
                                    .addOnCompleteListener { task: Task<AuthResult> ->
                                        if (task.isSuccessful) {
                                            // Sign in success, update UI with the signed-in user's information
                                            Log.d(ContentValues.TAG, "signInWithEmail:success")
                                            //val current_user = auth.currentUser
                                            //updateUI(user)
                                        } else {
                                            // If sign in fails, display a message to the user.
                                            Log.w(ContentValues.TAG, "signInWithEmail:failure", task.exception)
                                            Toast.makeText(
                                                context,
                                                "Authentication failed.",
                                                Toast.LENGTH_SHORT,
                                            ).show()
                                            Toast.makeText(
                                                context,
                                                task.exception?.message,
                                                Toast.LENGTH_SHORT,
                                            ).show()
                                            //updateUI(null)
                                        }
                                    }
                            }catch (ex: Exception){
                                Toast.makeText(
                                    context,
                                    ex.message,
                                    Toast.LENGTH_SHORT,
                                ).show()
                            }
                        }
                        Toast.makeText(
                            context,
                            "user exists...",
                            Toast.LENGTH_SHORT,
                        ).show()
                    }
                }
            }
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(
                    context,
                    "Cancelled",
                    Toast.LENGTH_SHORT,
                ).show()
            }
        })
        return true
    }

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
            if (entry.username.equals(Global.currentUser!!.username, ignoreCase = true))
                filteredEntries.add(entry) }
        Global.entries = filteredEntries

        val categories = Global.categories
        val filteredCategories: MutableList<Category> = mutableListOf()
        categories.forEach { category ->
            if (category.username.equals(Global.currentUser!!.username, ignoreCase = true))
                filteredCategories.add(category) }
        Global.categories = filteredCategories
    }
}