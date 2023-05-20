package com.opsc.workmate.ui

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.icu.text.SimpleDateFormat
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import com.opsc.workmate.R
import java.util.Calendar
import java.util.Locale

class NewEntryFragment : Fragment() {

    //Variables
    private lateinit var btnStartTime: Button
    private lateinit var btnEndTime: Button
    private lateinit var btnDate: Button

    private val calendar: Calendar = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_new_entry, container, false)

        btnStartTime = view.findViewById(R.id.btnStartTimePicker)
        btnStartTime.setOnClickListener {
            showTimePickerDialog(btnStartTime)
        }

        btnEndTime = view.findViewById(R.id.btnEndTimePicker)
        btnEndTime.setOnClickListener {
            showTimePickerDialog(btnEndTime)
        }

        btnDate = view.findViewById(R.id.btnDatePicker)
        btnDate.setOnClickListener {
            showDatePickerDialog()
        }

        return view
    }

    private fun showTimePickerDialog(button: Button) {
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        val timePickerDialog = TimePickerDialog(
            requireContext(),
            TimePickerDialog.OnTimeSetListener { _, selectedHour, selectedMinute ->
                calendar.set(Calendar.HOUR_OF_DAY, selectedHour)
                calendar.set(Calendar.MINUTE, selectedMinute)

                // Update button text with the selected time
                val formattedTime = SimpleDateFormat("hh:mm a", Locale.getDefault()).format(calendar.time)
                button.text = formattedTime
            },
            hour,
            minute,
            false
        )

        timePickerDialog.show()
    }

    private fun showDatePickerDialog() {
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            requireContext(),
            DatePickerDialog.OnDateSetListener { _, selectedYear, selectedMonth, selectedDayOfMonth ->
                calendar.set(Calendar.YEAR, selectedYear)
                calendar.set(Calendar.MONTH, selectedMonth)
                calendar.set(Calendar.DAY_OF_MONTH, selectedDayOfMonth)

                // Update button text with the selected date
                val formattedDate = SimpleDateFormat("yyyy/MM/dd", Locale.getDefault()).format(calendar.time)
                btnDate.text = formattedDate
            },
            year,
            month,
            dayOfMonth
        )

        datePickerDialog.show()
    }

}