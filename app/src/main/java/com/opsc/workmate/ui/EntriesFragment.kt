package com.opsc.workmate.ui

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.opsc.workmate.R
import com.opsc.workmate.data.Category
import com.opsc.workmate.data.Entry
import com.opsc.workmate.data.EntryAdapter
import com.opsc.workmate.data.Global
import com.opsc.workmate.ui.EntryFragment
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.logging.Logger.global

class EntriesFragment : Fragment(), EntryAdapter.OnItemClickListener {

    private lateinit var btnStartDate: Button
    private lateinit var btnEndDate: Button
    private lateinit var btnCategory: Button
    private lateinit var btnFilter: Button
    private lateinit var lstEntriesFilter: RecyclerView
    private lateinit var categoryNames: List<String>
    private lateinit var txtTimeSpent: TextView

    private val calendar: Calendar = Calendar.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_entries, container, false)

        btnStartDate = view.findViewById(R.id.btnStartDate)
        btnEndDate = view.findViewById(R.id.btnEndDate)
        btnFilter = view.findViewById(R.id.btnFilter)
        lstEntriesFilter = view.findViewById(R.id.lstEntriesFilter)
        btnCategory = view.findViewById(R.id.btnEntriesCategoryPicker)

        btnStartDate.setOnClickListener {
            showDatePickerDialog(btnStartDate)
        }

        btnEndDate.setOnClickListener {
            showDatePickerDialog(btnEndDate)
        }

        categoryNames = Global.categories.map { it.name } // Retrieve category names from Global.categories
        btnCategory.setOnClickListener {
            showCategoryPickerDialog()
        }

        lstEntriesFilter.layoutManager = LinearLayoutManager(requireContext())
        var adapter = EntryAdapter(Global.entries)
        adapter.setOnItemClickListener(this)
        lstEntriesFilter.adapter = adapter

        btnFilter.setOnClickListener {
            filterEntries()
        }

        return view
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

    private fun filterEntries() {
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
        val btnEntriesCategoryPicker : Button = requireView().findViewById(R.id.btnEntriesCategoryPicker)
        val categoryName = btnEntriesCategoryPicker.text.toString()

        val entries = Global.entries
        val filteredEntries: MutableList<Entry> = mutableListOf()

        //Filter entries and add to list
        entries.forEach { entry ->
            val entryDate = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).parse(entry.date)
            if ( entryDate.after(startDate) && entryDate.before(endDate) && entry.categoryName.equals(categoryName, ignoreCase = true))
                filteredEntries.add(entry)
        }

        //Calculate Time Spent
        val timeSpent = Category.getTotalHours(filteredEntries)
        txtTimeSpent = requireView().findViewById(R.id.txtTimeSpentFiltered)
        txtTimeSpent.text = "$timeSpent hrs"

        //Display entries
        var adapter = EntryAdapter(filteredEntries)
        adapter.setOnItemClickListener(this)
        lstEntriesFilter.adapter = adapter

    }

    override fun onItemClick(entry: Entry) {
        // Handle the click event and navigate to a different fragment
        //Add data to bundle
        val bundle = Bundle()
        bundle.putString("username", entry.username)
        bundle.putString("category", entry.categoryName)
        bundle.putString("date", entry.date)
        bundle.putString("startTime", entry.startTime)
        bundle.putString("endTime", entry.endTime)
        bundle.putString("imageData", entry.imageData)
        bundle.putString("description", entry.description)

        val fragment = EntryFragment()
        fragment.arguments = bundle

        //Navigate to fragment, passing bundle
        findNavController().navigate(R.id.action_dashboardFragment_to_entryFragment, bundle)
    }

}
