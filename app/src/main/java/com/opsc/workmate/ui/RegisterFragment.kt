package com.opsc.workmate.ui

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.Navigation
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.opsc.workmate.R
import com.opsc.workmate.data.Global
import com.opsc.workmate.data.User

class RegisterFragment : Fragment() {
    private lateinit var auth: FirebaseAuth
    private var firebaseReference: FirebaseDatabase? = null
    private var userDatabaseReference: DatabaseReference? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
        firebaseReference = FirebaseDatabase.getInstance()
        userDatabaseReference = firebaseReference?.getReference("User")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_register, container, false)
        val txtLogIn: TextView = view.findViewById(R.id.txtLogIn)

        txtLogIn.setOnClickListener {
            val navController = Navigation.findNavController(view)
            navController.navigate(R.id.action_registerFragment_to_loginFragment)
        }

        val btnRegister: Button = view.findViewById(R.id.btnRegister)

        btnRegister.setOnClickListener {
            RegisterUser()
        }

        return view
    }

    private fun RegisterUser() {
        val txtUsername: EditText = requireView().findViewById(R.id.txtUsername)
        val txtPassword: EditText = requireView().findViewById(R.id.txtPassword)
        val txtConfirmPassword: EditText = requireView().findViewById(R.id.txtConfirmPassword)
        val txtFullName: EditText = requireView().findViewById(R.id.txtName)
        val txtEmail: EditText = requireView().findViewById(R.id.txtEmail)

        val username = txtUsername.text.toString()
        val password = txtPassword.text.toString()
        val confirmPassword = txtConfirmPassword.text.toString()
        val fullName = txtFullName.text.toString()
        val email = txtEmail.text.toString()
        val freeCoins = 10

        if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() || fullName.isEmpty() || email.isEmpty()) {
            Toast.makeText(requireContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show()
            return
        }

        if (password != confirmPassword) {
            Toast.makeText(requireContext(), "Passwords do not match", Toast.LENGTH_SHORT).show()
            return
        }

        val isUsernameTaken = Global.users.any { it.username == username }
        if (isUsernameTaken) {
            Toast.makeText(requireContext(), "Username Taken!", Toast.LENGTH_SHORT).show()
            return
        }

        val isEmailTaken = Global.users.any { it.email == email }
        if (isEmailTaken) {
            Toast.makeText(requireContext(), "Email Taken!", Toast.LENGTH_SHORT).show()
            return
        }

        val newUser = User(null, username, password, email, fullName, freeCoins)


        if (newUser != null) {
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task: Task<AuthResult> ->
                    if (task.isSuccessful) {
                        val firebaseUser = auth.currentUser
                        val uid = firebaseUser?.uid
                        newUser.uid = uid

                        userDatabaseReference?.child(uid ?: "")?.setValue(newUser)

                        //Navigate
                        val navController = Navigation.findNavController(requireView())
                        Toast.makeText(activity, "User Registered!", Toast.LENGTH_SHORT).show()
                        navController.navigate(R.id.action_registerFragment_to_loginFragment)

                    } else {
                        Log.w(ContentValues.TAG, "createUserWithEmail:failure", task.exception)

                        Toast.makeText(context, task.exception?.message, Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }
}
