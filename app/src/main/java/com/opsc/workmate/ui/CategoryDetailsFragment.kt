package com.opsc.workmate.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.opsc.workmate.R
import com.opsc.workmate.data.Image

class CategoryDetailsFragment : Fragment() {

    lateinit var txtCategory : TextView
    lateinit var imgCategoryImage : ImageView

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

        }
    }

}