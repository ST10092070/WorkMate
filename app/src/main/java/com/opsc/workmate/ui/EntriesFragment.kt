package com.opsc.workmate.ui

import android.app.DatePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import com.opsc.workmate.R
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class EntriesFragment : Fragment() {

    //Variables
    private lateinit var btnStartDate: Button
    private lateinit var btnEndDate: Button

    private val calendar: Calendar = Calendar.getInstance()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_entries, container, false)

        btnStartDate = view.findViewById(R.id.btnStartDate)
        btnStartDate.setOnClickListener {
            showDatePickerDialog(btnStartDate)
        }

        btnEndDate = view.findViewById(R.id.btnEndDate)
        btnEndDate.setOnClickListener {
            showDatePickerDialog(btnEndDate)
        }

        return view
    }

    private fun showDatePickerDialog(button: Button) {
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            requireContext(),
            DatePickerDialog.OnDateSetListener { _, selectedYear, selectedMonth, selectedDayOfMonth ->
                calendar.set(Calendar.YEAR, selectedYear)
                calendar.set(Calendar.MONTH, selectedMonth)
                calendar.set(Calendar.DAY_OF_MONTH, selectedDayOfMonth)

                val selectedDate = calendar.time
                val formattedDate = SimpleDateFormat("yyyy/MM/dd", Locale.getDefault()).format(selectedDate)
                button.text = formattedDate

                if (button.id == R.id.btnEndDate) {
                    val startDateText = btnStartDate.text.toString()
                    val startDate = SimpleDateFormat("yyyy/MM/dd", Locale.getDefault()).parse(startDateText)

                    if (startDate != null && selectedDate.before(startDate)) {
                        // Clear the end date if it is before the start date
                        btnEndDate.text = ""
                        // Display an error or a toast message indicating invalid selection
                        // For example:
                        Toast.makeText(requireContext(), "End date cannot be before start date", Toast.LENGTH_SHORT).show()
                    }
                }
            },
            year,
            month,
            dayOfMonth
        )

        datePickerDialog.show()
    }

}