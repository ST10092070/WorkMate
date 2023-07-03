package com.opsc.workmate.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import com.opsc.workmate.R
import com.opsc.workmate.data.DataManager
import com.opsc.workmate.data.Entry
import com.opsc.workmate.data.Global
import com.opsc.workmate.data.Goal
import java.text.SimpleDateFormat
import java.time.Duration
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Locale

class GoalsFragment : Fragment() {

    lateinit var btnSetGoals: Button
    lateinit var txtMax: TextView
    lateinit var txtMin: TextView
    lateinit var zeroToMinProgress: ProgressBar
    lateinit var minToMaxProgress: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_goals, container, false)

        //Set onClick for Set Goals button
        btnSetGoals = view.findViewById(R.id.btnSetGoals)
        btnSetGoals.setOnClickListener {
            //navigate
            findNavController().navigate(R.id.action_goalsFragment_to_setGoalsFragment)
        }

        return view

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Get values, return if null

        val uid = Global.currentUser!!.uid.toString()

        //Get user goal and set local data
        var goal: Goal = Goal(uid, "00:00:00", "00:00:00") //Initialise variable
        DataManager.getGoal(uid) { userGoal ->  //Update variable
            if (userGoal != null) {
                goal = userGoal
                updateGoalDisplay(goal)
            }
        }
    }

    private fun updateGoalDisplay(goal: Goal) {
        txtMin = requireView().findViewById(R.id.txtMin)
        txtMax = requireView().findViewById(R.id.txtMax)
        zeroToMinProgress = requireView().findViewById(R.id.bar_zeroToMin)
        minToMaxProgress = requireView().findViewById(R.id.bar_minToMax)

        val minTime = goal.minGoal
        val maxTime = goal.maxGoal

        //Filter entries and add to list
        //Update local entries
        DataManager.getEntries(Global.currentUser!!.uid.toString()) { entries -> Global.entries = entries }

        var entries = Global.entries
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val today = Calendar.getInstance()

        today.set(Calendar.HOUR_OF_DAY, 0)
        today.set(Calendar.MINUTE, 0)
        today.set(Calendar.SECOND, 0)
        today.set(Calendar.MILLISECOND, 0)

        val filteredEntries: MutableList<Entry> = mutableListOf()
        entries.forEach { entry ->
            val entryDate = dateFormat.parse(entry.date)

            if (entryDate != null) {
                val calendar = Calendar.getInstance()
                calendar.time = entryDate

                val entryDay = calendar.get(Calendar.DAY_OF_MONTH)
                val entryMonth = calendar.get(Calendar.MONTH)
                val entryYear = calendar.get(Calendar.YEAR)

                val todayDay = today.get(Calendar.DAY_OF_MONTH)
                val todayMonth = today.get(Calendar.MONTH)
                val todayYear = today.get(Calendar.YEAR)

                if (entryDay == todayDay && entryMonth == todayMonth && entryYear == todayYear) {
                    filteredEntries.add(entry)
                }
            }
        }

        //Use filtered list to calculate total time spent today
        val duration = calculateTotalTimeSpent(filteredEntries)

        //Convert to LocalTime
        val lMinTime = LocalTime.parse(minTime)
        val lMaxTime = LocalTime.parse(maxTime)
        //get min and max as minutes
        var minMinutes = lMinTime.hour * 60 + lMinTime.minute
        var maxMinutes = lMaxTime.hour * 60 + lMaxTime.minute



        //Set fields to current time
        txtMin.text = "MIN : $minTime"
        txtMax.text = "MAX : $maxTime"

        //Calculate progress bars data
        var zeroToMinPercentage = (duration.toFloat() / minMinutes) * 100
        var minToMaxPercentage = 0F

        if (zeroToMinPercentage >= 100) {
            zeroToMinPercentage = 100F
            minToMaxPercentage = (duration.toFloat() / maxMinutes) * 100
        }

        //Set progress bar percentage
        zeroToMinProgress.progress = zeroToMinPercentage.toInt()
        minToMaxProgress.progress = minToMaxPercentage.toInt()


    }

    private fun calculateTotalTimeSpent(entries: List<Entry>): Long {
        var totalTimeSpent: Long = 0

        for (entry in entries) {
            val startTime = LocalTime.parse(entry.startTime, DateTimeFormatter.ofPattern("hh:mm a"))
            val endTime = LocalTime.parse(entry.endTime, DateTimeFormatter.ofPattern("hh:mm a"))
            val duration = Duration.between(startTime, endTime).toMinutes()
            totalTimeSpent += duration
        }

        return totalTimeSpent
    }
}