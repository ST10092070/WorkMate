package com.opsc.workmate.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.Navigation
import com.opsc.workmate.R
import com.opsc.workmate.data.Global
import com.opsc.workmate.data.User

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
            val isValid = RegisterUser()

            if (isValid) {
                // Get the NavController
                val navController = Navigation.findNavController(view)
                Toast.makeText(activity, "User Registered!", Toast.LENGTH_SHORT).show()

                // Navigate to the registerFragment
                navController.navigate(R.id.action_registerFragment_to_loginFragment)
            } // else : feedback done in method
        }

        return view
    }

    private fun RegisterUser(): Boolean {
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

        // Perform input validation
        if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() || fullName.isEmpty() || email.isEmpty()) {
            Toast.makeText(requireContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show()
            return false
        }

        if (password != confirmPassword) {
            Toast.makeText(requireContext(), "Passwords do not match", Toast.LENGTH_SHORT).show()
            return false
        }

        // Check if the username is already taken
        val isUsernameTaken = Global.users.any { it.username == username }
        if (isUsernameTaken) {
            Toast.makeText(requireContext(), "Username Taken!", Toast.LENGTH_SHORT).show()
            return false
        }

        val isEmailTaken = Global.users.any { it.email == email }
        if (isEmailTaken) {
            Toast.makeText(requireContext(), "Email Taken!", Toast.LENGTH_SHORT).show()
            return false
        }

        // Create a new User object
        val newUser = User(username, password, email, fullName)

        // Add the user to the Global.users list
        Global.users.add(newUser)

        return true
    }
}