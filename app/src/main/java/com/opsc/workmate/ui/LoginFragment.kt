package com.opsc.workmate.ui

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.Navigation
import com.opsc.workmate.MainActivity
import com.opsc.workmate.R

class LoginFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
        return view
    }

    private fun validateLogin(): Boolean {
        //TODO: Implement validation for login


        //Use this method, return true for successful, return false for fail
        return true
    }
}