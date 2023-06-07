package com.opsc.workmate.ui

import android.os.Bundle
import android.text.InputFilter
import android.text.Spanned
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.opsc.workmate.R
import com.opsc.workmate.data.Global
import com.opsc.workmate.data.Goal
import java.time.LocalTime
import java.time.format.DateTimeFormatter

class SetGoalsFragment : Fragment() {

    lateinit var edtMinTime: TextView
    lateinit var edtMaxTime: TextView
    lateinit var btnSetGoals: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_set_goals, container, false)

        edtMinTime = view.findViewById(R.id.edtMinTime)
        edtMaxTime = view.findViewById(R.id.edtMaxTime)

        edtMinTime.filters = arrayOf(TimeInputFilter())
        edtMaxTime.filters = arrayOf(TimeInputFilter())

        //Set onClick for set button
        btnSetGoals = view.findViewById(R.id.btnConfirmGoals)
        btnSetGoals.setOnClickListener {

            val success = setGoals(edtMinTime.text.toString(), edtMaxTime.text.toString(), Global.currentUser!!.username)

            if (success) {
                //Navigate and confirmation
                Toast.makeText(activity, "Goals set!", Toast.LENGTH_SHORT).show()
                findNavController().navigate(R.id.action_setGoalsFragment_to_goalsFragment)
            }


        }

        return view
    }

    private fun setGoals(minTimeString: String, maxTimeString: String, username: String): Boolean {
        //Validate time strings
        val minTime = convertTimeStringToTime(minTimeString)
        val maxTime = convertTimeStringToTime(maxTimeString)
        if (minTime == null || maxTime == null)
            return false

        //Create object using data
        val goal = Goal(username, minTime, maxTime)

        //Add to program data
        Global.goals.add(goal)

        return true

    }

    private fun convertTimeStringToTime(timeString: String): LocalTime? {
        val format = DateTimeFormatter.ofPattern("HH:mm")
        return try {
            LocalTime.parse(timeString, format)
        } catch (e: Exception) {
            e.printStackTrace()
            null
            //Show toast
        }
    }
}

//Filter class to limit what the user can type
//Will only allow the user to type two numbers a ":" and another two numbers
class TimeInputFilter : InputFilter {

    companion object {
        private const val MAX_LENGTH = 5
    }

    override fun filter(
        source: CharSequence?,
        start: Int,
        end: Int,
        dest: Spanned?,
        dstart: Int,
        dend: Int
    ): CharSequence? {
        val inputLength = (dest?.length ?: 0) + (end - start)

        if (inputLength > MAX_LENGTH) {
            // Input exceeds the maximum allowed length
            return ""
        }

        val allowedChars = if (dstart in 0..1) {
            // Allow only digits for the first two characters
            arrayListOf('0', '1', '2', '3', '4', '5', '6', '7', '8', '9')
        } else if (dstart == 2) {
            // Allow only the colon character after the first two characters
            arrayListOf(':')
        } else {
            // Allow only digits after the colon character
            arrayListOf('0', '1', '2', '3', '4', '5', '6', '7', '8', '9')
        }

        val filteredStringBuilder = StringBuilder()

        for (i in start until end) {
            val currentChar = source?.get(i)
            if (currentChar != null && allowedChars.contains(currentChar)) {
                filteredStringBuilder.append(currentChar)
            }
        }

        return filteredStringBuilder.toString()
    }
}
