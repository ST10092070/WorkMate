package com.opsc.workmate.ui

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.graphics.Color
import android.os.Bundle
import android.util.Half.toFloat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.opsc.workmate.R
import com.opsc.workmate.data.DataManager
import com.opsc.workmate.data.Entry
import com.opsc.workmate.data.Global
import java.text.SimpleDateFormat
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

        DataManager.getGoal(Global.currentUser!!.uid.toString()) { goal -> Global.goal = goal!! } //Get current goals

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

        if (filteredEntries.isEmpty()) {
            Toast.makeText(requireContext(), "No records to display", Toast.LENGTH_SHORT).show()
            return
        }

        filteredEntries = filteredEntries.sortedBy { entry -> entry.date } as MutableList<Entry>

        //Create and display lineChart
        updateChart(filteredEntries)
    }

    private fun updateChart(filteredEntries: MutableList<Entry>) {
        //Initialise LineChart variable
        val chart : LineChart = requireView().findViewById(com.opsc.workmate.R.id.lineChart)

        //Create list that contains total times on dates and dates list for labels
        data class TimeEntry(val date: String, var value: Float)
        val lstTimes: ArrayList<TimeEntry> = ArrayList()
        val lstDates = ArrayList<String>()

        for (i in 0..filteredEntries.lastIndex) {
            val entry = filteredEntries[i]
            val timeSpentMillis = calculateDuration(entry.startTime, entry.endTime)
            var found = false
            var pos = 0
            for (item in lstTimes) {
                if (item.date.equals(entry.date)) {
                    //Add to list in existing pos
                    lstTimes[pos].value = lstTimes[pos].value.toFloat() + ( timeSpentMillis.toFloat() / (1000 * 60 * 60) )
                    found = true
                    break
                }
                pos ++
            }

            //If not found, create new
            if (!found) {
                lstDates.add((entry.date))
                lstTimes.add(TimeEntry(entry.date, timeSpentMillis.toFloat() / (1000 * 60 * 60)))
            }
        }

        var xAxis = chart.xAxis
        xAxis.valueFormatter = IndexAxisValueFormatter(lstDates)
        xAxis.textSize = 10f

        //---------------------------------------------------------

        //Create list of entries that correspond to dates and dataset
        val lstEntries : ArrayList<com.github.mikephil.charting.data.Entry> = ArrayList()
        var counter = 0

        lstTimes.forEach { timeEntry -> lstEntries.add(com.github.mikephil.charting.data.Entry(counter++.toFloat(), timeEntry.value)) }

        val ldsEntries = LineDataSet(lstEntries, "Total Time")
        //Set display styling
        ldsEntries.color = Color.BLUE
        ldsEntries.setCircleColor(Color.BLUE)
        ldsEntries.circleRadius = 5f
        ldsEntries.valueTextSize = 12f
        ldsEntries.lineWidth = 2f
        // Set the mode to apply cubic Bezier curve interpolation
        ldsEntries.mode = LineDataSet.Mode.CUBIC_BEZIER

        //Create Line for Min and Max Goals
        val lstGoalMin : ArrayList<com.github.mikephil.charting.data.Entry> = ArrayList()
        val lstGoalMax : ArrayList<com.github.mikephil.charting.data.Entry> = ArrayList()

        DataManager.getGoal(Global.currentUser!!.uid.toString()) { goal -> Global.goal = goal!! } //Get current goals
        val minGoal = Global.goal.minGoal
        val maxGoal = Global.goal.maxGoal

        //Set first points
        var entryPos = 0F
        lstGoalMin.add(com.github.mikephil.charting.data.Entry(entryPos, calculateDuration("00:00", minGoal).toFloat() / (1000 * 60 * 60)))
        lstGoalMax.add(com.github.mikephil.charting.data.Entry(entryPos, calculateDuration("00:00", maxGoal).toFloat() / (1000 * 60 * 60)))

        //Set last points
        entryPos = lstTimes.lastIndex.toFloat()
        lstGoalMin.add(com.github.mikephil.charting.data.Entry(entryPos, calculateDuration("00:00", minGoal).toFloat() / (1000 * 60 * 60)))
        lstGoalMax.add(com.github.mikephil.charting.data.Entry(entryPos, calculateDuration("00:00", maxGoal).toFloat() / (1000 * 60 * 60)))

        //Create LineDataSets for mingoal and maxgoal
        val ldsMinGoal = LineDataSet(lstGoalMin, "Min Goal")
        ldsMinGoal.color = Color.YELLOW
        ldsMinGoal.setCircleColor(Color.YELLOW)
        ldsMinGoal.circleRadius = 0f
        ldsMinGoal.valueTextSize = 0f
        ldsMinGoal.lineWidth = 5f

        val ldsMaxGoal = LineDataSet(lstGoalMax, "Max Goal")
        ldsMaxGoal.color = Color.GREEN
        ldsMaxGoal.setCircleColor(Color.GREEN)
        ldsMaxGoal.circleRadius = 0f
        ldsMaxGoal.valueTextSize = 0f
        ldsMaxGoal.lineWidth = 5f



        //Display chart
        val data = LineData(ldsEntries, ldsMinGoal, ldsMaxGoal)

        chart.data = data
        chart.axisLeft.textSize = 12f
        chart.axisRight.textSize = 12f
        chart.description.text = ""
        chart.legend.textSize = 12f
        chart.legend.direction =  Legend.LegendDirection.RIGHT_TO_LEFT
        chart.setBackgroundColor(resources.getColor(R.color.lightGray))
        chart.animateY(3000)
        chart.setDrawGridBackground(false)

        chart.invalidate()
    }

    fun convertTimeToMilliseconds(timeString: String): Long {
        val format = SimpleDateFormat("HH:mm")
        val date: Date = format.parse(timeString)
        return date.time
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