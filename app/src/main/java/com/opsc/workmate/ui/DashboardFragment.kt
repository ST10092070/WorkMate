package com.opsc.workmate.ui

import com.opsc.workmate.data.EntryAdapter
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.opsc.workmate.R
import com.opsc.workmate.data.CategoryAdapter
import com.opsc.workmate.data.Global

class DashboardFragment : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_dashboard, container, false)

        //implement RecyclerView for Categories -------------
        // Find the RecyclerView by ID
        val lstCategories: RecyclerView = view.findViewById(R.id.lstCategories)
        val lstEntries: RecyclerView = view.findViewById(R.id.lstEntries)

        // Set up the LinearLayoutManager for the RecyclerView
        val catLayoutManager = LinearLayoutManager(requireContext())
        lstCategories.layoutManager = catLayoutManager

        // Create and set the CategoryAdapter with the Global.categories list
        val catAdapter = CategoryAdapter(Global.categories)
        lstCategories.adapter = catAdapter

        // Set up the LinearLayoutManager for the RecyclerView
        val entryLayoutManager = LinearLayoutManager(requireContext())
        lstEntries.layoutManager = entryLayoutManager

        // Create and set the EntryAdapter with the Global.entries list
        val adapter = EntryAdapter(Global.entries)
        lstEntries.adapter = adapter



        //---- Navigate to Create Category screen ----
        // Find the btn by ID
        val btnCreate: Button = view.findViewById(R.id.btnCreate)

        // Set onClickListener for the TextView
        btnCreate.setOnClickListener {
            // Get the NavController
            val navController = Navigation.findNavController(view)

            // Navigate to the CreateCategoryFragment
            navController.navigate(R.id.action_dashboardFragment_to_createCategoryFragment)
        }

        return view
    }
}