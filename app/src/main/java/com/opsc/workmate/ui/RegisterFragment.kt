package com.opsc.workmate.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.Navigation
import com.opsc.workmate.R
class RegisterFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_register, container, false)

        //---- Navigate to register screen ----
        // Find the TextView by ID
        val txtLogIn: TextView = view.findViewById(R.id.txtLogIn)

        // Set onClickListener for the TextView
        txtLogIn.setOnClickListener {
            // Get the NavController
            val navController = Navigation.findNavController(view)

            // Navigate to the registerFragment using the action defined in the nav_graph_login.xml
            navController.navigate(R.id.action_registerFragment_to_loginFragment)
        }

        //---- Register Button ----
        // Find the Button by ID
        val btnRegister: Button = view.findViewById(R.id.btnRegister)

        // Set OnClickListener for the Button
        btnRegister.setOnClickListener {
            // Perform input validation
            val isValid = validateInput()

            if (isValid) {
                // Get the NavController
                val navController = Navigation.findNavController(view)
                // Navigate to the registerFragment
                navController.navigate(R.id.action_registerFragment_to_loginFragment)
            } else {
                // Display an error message or handle the invalid input case
                Toast.makeText(activity, "Invalid input", Toast.LENGTH_SHORT).show()
            }
        }

        return view
    }

    private fun validateInput(): Boolean {
        //TODO: Implement registration
        return true
    }
}