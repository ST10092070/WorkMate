package com.opsc.workmate.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.opsc.workmate.R
import com.opsc.workmate.data.CategoryAdapter
import com.opsc.workmate.data.Entry
import com.opsc.workmate.data.EntryAdapter
import com.opsc.workmate.data.Global
import com.opsc.workmate.data.Image

class CategoryDetailsFragment : Fragment(), EntryAdapter.OnItemClickListener {

    lateinit var txtCategory: TextView
    lateinit var imgCategoryImage: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_category_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Retrieve the arguments Bundle
        val arguments = arguments

        // Check if arguments exist
        if (arguments != null) {
            // Retrieve the data from the bundle
            val username = arguments.getString("username")
            val name = arguments.getString("name")
            val colour = arguments.getInt("colour")
            val imageData = arguments.getString("imageData")

            //Update UI elements---

            //Get UI elements
            txtCategory = view.findViewById(R.id.txtCategoryDetailsName)
            imgCategoryImage = view.findViewById(R.id.imgCategoryImage)

            //Set Values
            txtCategory.text = name
            Image.setBase64Image(imageData, imgCategoryImage)

            //---get and set filtered entry list---

            // Get the filtered list of entries based on the category name
            val filteredEntries = Global.entries.filter { entry ->
                entry.categoryName == name
            }

            val lstEntries: RecyclerView = view.findViewById(R.id.lstCategoryEntries)
            // Set up the LinearLayoutManager for the RecyclerView
            val entryLayoutManager = LinearLayoutManager(requireContext())
            lstEntries.layoutManager = entryLayoutManager

            // Create an instance of EntryAdapter with the filtered list
            val adapter = EntryAdapter(filteredEntries)

            // Set the item click listener
            adapter.setOnItemClickListener(this)

            // Set the adapter to the RecyclerView
            lstEntries.adapter = adapter

        }

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

        val fragment = EntryFragment()
        fragment.arguments = bundle

        //Navigate to fragment, passing bundle
        findNavController().navigate(R.id.action_dashboardFragment_to_entryFragment, bundle)
    }

}
