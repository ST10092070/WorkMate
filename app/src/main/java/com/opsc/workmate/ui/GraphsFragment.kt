package com.opsc.workmate.ui

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.graphics.Color
import android.os.Build.VERSION_CODES.M
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.formatter.ValueFormatter
import com.opsc.workmate.R
import com.opsc.workmate.data.Entry
import com.opsc.workmate.data.Global
import java.text.SimpleDateFormat
import java.time.Duration
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Date
import java.util.Locale


class GraphsFragment : Fragment() {

    private lateinit var btnStartDate: Button
    private lateinit var btnEndDate: Button
    private lateinit var btnCategory: Button
    private lateinit var btnFilter: Button
    private lateinit var categoryNames: List<String>
    private val calendar: Calendar = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(com.opsc.workmate.R.layout.fragment_graphs, container, false)

        //Make evrything on UI work ----
        btnStartDate = view.findViewById(R.id.btnStartDate)
        btnEndDate = view.findViewById(R.id.btnEndDate)
        btnFilter = view.findViewById(R.id.btnFilter)

        btnStartDate.setOnClickListener {
            showDatePickerDialog(btnStartDate)
        }

        btnEndDate.setOnClickListener {
            showDatePickerDialog(btnEndDate)
        }

        btnFilter.setOnClickListener {
            filterEntries()
        }

        return view
    }

    private fun filterEntries() {
        //Get dates from inputs and do validation
        val startDateText = btnStartDate.text.toString()
        val endDateText = btnEndDate.text.toString()

        if (startDateText.isBlank() || endDateText.isBlank() || startDateText.equals("dd/mm/yyyy") || endDateText.equals("dd/mm/yyyy")) {
            Toast.makeText(requireContext(), "Please select start and end dates", Toast.LENGTH_SHORT).show()
            return
        }

        val startDate = SimpleDateFormat("yyyy/MM/dd", Locale.getDefault()).parse(startDateText)
        val endDate = SimpleDateFormat("yyyy/MM/dd", Locale.getDefault()).parse(endDateText)

        if (startDate != null && endDate != null && startDate.after(endDate)) {
            Toast.makeText(requireContext(), "End date cannot be before start date", Toast.LENGTH_SHORT).show()
            return
        }

        //Initialise LineChart variable
        val chart : LineChart = requireView().findViewById(com.opsc.workmate.R.id.lineChart)

        // Get Entries List
        val entries = Global.entries
        var filteredEntries: MutableList<Entry> = mutableListOf()

        // Filter entries and add to list
        entries.forEach { entry ->
            val entryDate = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).parse(entry.date)
            if (entryDate != null && entryDate.after(startDate) && entryDate.before(endDate)) {
                filteredEntries.add(entry)
            }
        }
        //Order filteredEntries to date
        filteredEntries = filteredEntries.sortedBy { entry -> entry.date } as MutableList<Entry>

        //Create and display lineChart
        updateChart(filteredEntries)
    }

    private fun updateChart(filteredEntries: MutableList<Entry>) {
        //Initialise LineChart variable
        val chart : LineChart = requireView().findViewById(com.opsc.workmate.R.id.lineChart)

        //Order filteredEntries to date
        val orderedEntries = filteredEntries.sortedBy { entry -> entry.date }

        //Create list that contains dates of entries
        val lstDates = ArrayList<String>()
        filteredEntries.forEach { entry ->
            lstDates.add(entry.date)
        }

        //Create list of entries that correspond to dates
        val lstEntries : ArrayList<com.github.mikephil.charting.data.Entry> = ArrayList()
        var counter = 0
        filteredEntries.forEach { entry ->
            //Calculate time spent to set as y value
            val timeSpentMillis = calculateDuration(entry.startTime, entry.endTime)

            lstEntries.add(com.github.mikephil.charting.data.Entry(counter++.toFloat(), timeSpentMillis.toFloat()/1000000))
        }


        //Create DataSet for entries
        val lineDataSet = LineDataSet(lstEntries, "Total Time")

        //Set display styling
        lineDataSet.color = Color.GREEN
        lineDataSet.setCircleColor(Color.BLUE)
        lineDataSet.circleRadius = 5f
        lineDataSet.valueTextSize = 12f
        // Set the mode to apply cubic Bezier curve interpolation
        lineDataSet.mode = LineDataSet.Mode.CUBIC_BEZIER

        val xAxis = chart.xAxis
        xAxis.valueFormatter = IndexAxisValueFormatter(lstDates)
        xAxis.textSize = 12f

        val data = LineData(lineDataSet)

        chart.data = data
        chart.axisLeft.textSize = 12f
        chart.axisRight.textSize = 12f
        chart.description.text = ""
        chart.setBackgroundColor(resources.getColor(R.color.lightGray))
        chart.animateY(3000)
        chart.setDrawGridBackground(false)

        chart.invalidate()
    }

    private fun calculateDuration(startTime: String, endTime: String): Long {
        // Assuming the time format is "hh:mm"
        val startHour = startTime.substring(0, 2).toInt()
        val startMinute = startTime.substring(3, 5).toInt()
        val endHour = endTime.substring(0, 2).toInt()
        val endMinute = endTime.substring(3, 5).toInt()

        val startMillis = startHour.toLong() * 60 * 60 * 1000 + startMinute.toLong() * 60 * 1000
        val endMillis = endHour.toLong() * 60 * 60 * 1000 + endMinute.toLong() * 60 * 1000

        return endMillis - startMillis
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    private fun showCategoryPickerDialog() {
        val categoryArray = categoryNames.toTypedArray()
        val selectedCategoryIndex = categoryArray.indexOf(btnCategory.text.toString())

        AlertDialog.Builder(requireContext())
            .setTitle("Select Category")
            .setSingleChoiceItems(categoryArray, selectedCategoryIndex) { dialog, which ->
                btnCategory.text = categoryArray[which]
                dialog.dismiss()
            }
            .show()
    }

    private fun showDatePickerDialog(button: Button) {

        //Code Attribution
        //The below code was derived from StackOverflow
        //https://stackoverflow.com/questions/45842167/how-to-use-datepickerdialog-in-kotlin
        //Alexandr Kovalenko
        //https://stackoverflow.com/users/7697901/alexandr-kovalenko

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
                val formattedDate = android.icu.text.SimpleDateFormat(
                    "yyyy/MM/dd",
                    Locale.getDefault()
                ).format(calendar.time)
                button.text = formattedDate
            },
            year,
            month,
            dayOfMonth
        )

        datePickerDialog.show()
    }
}