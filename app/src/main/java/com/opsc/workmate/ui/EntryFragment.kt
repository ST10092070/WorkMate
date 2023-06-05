package com.opsc.workmate.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.opsc.workmate.R
import com.opsc.workmate.data.Image
import org.w3c.dom.Text

class EntryFragment : Fragment() {

    lateinit var txtCategory : TextView
    lateinit var txtDate : TextView
    lateinit var txtTime : TextView
    lateinit var imgEntryImage : ImageView
    lateinit var txtDescription : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_entry, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Retrieve the arguments Bundle
        val arguments = arguments

        // Check if arguments exist
        if (arguments != null) {
            // Retrieve the data from the bundle
            val username = arguments.getString("username")
            val category = arguments.getString("category")
            val date = arguments.getString("date")
            val startTime = arguments.getString("startTime")
            val endTime = arguments.getString("endTime")
            val imageData = arguments.getString("imageData")
            val description = arguments.getString("description")
            //Update UI elements---

            //Get UI elements
            txtCategory = view.findViewById(R.id.txtCategory)
            txtDate = view.findViewById(R.id.txtDate)
            txtTime = view.findViewById(R.id.txtTime)
            imgEntryImage = view.findViewById(R.id.imgEntryImage)
            txtDescription = view.findViewById(R.id.txtEntryDetailsDescription)

            //Set Values
            txtCategory.text = category
            txtDate.text = date
            txtTime.text = startTime + " -> " + endTime
            Image.setBase64Image(imageData, imgEntryImage)
            txtDescription.text = description

        }
    }

}