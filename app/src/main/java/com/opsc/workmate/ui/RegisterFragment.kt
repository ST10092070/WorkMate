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

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [RegisterFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class RegisterFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
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

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment RegisterFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            RegisterFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}