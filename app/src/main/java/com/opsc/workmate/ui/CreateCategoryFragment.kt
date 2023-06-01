package com.opsc.workmate.ui

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.opsc.workmate.R
import eltos.simpledialogfragment.SimpleDialog
import eltos.simpledialogfragment.color.SimpleColorDialog
import eltos.simpledialogfragment.color.SimpleColorWheelDialog


class CreateCategoryFragment : Fragment(), SimpleDialog.OnDialogResultListener {

    //Variables
    private lateinit var btnChooseColour: Button
    private lateinit var btnCreate: Button
    private lateinit var imgCategoryImage: ImageView
    private var selectedColor: Int = Color.WHITE

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_create_category, container, false)

        btnChooseColour = view.findViewById(R.id.btnChooseColour)
        btnCreate = view.findViewById(R.id.btnCreate)
        imgCategoryImage = view.findViewById(R.id.imgCategoryImage)

        //Implement colour picker
        btnChooseColour.setOnClickListener {
            showColorPickerDialog()
        }

        //rest goes here

        return view

    }


    private fun showColorPickerDialog() {
        SimpleColorWheelDialog.build()
            .color(0x80e9a11d.toInt())
            .alpha(false)
            .hideHexInput(true)
            .show(this, "Tag");
    }

    //handle colour picker results
    override fun onResult(dialogTag: String, which: Int, extras: Bundle): Boolean {
        if (dialogTag == "Tag") {
            val color = extras?.getInt(SimpleColorWheelDialog.COLOR, Color.WHITE)
            if (which == -1 && color != null) {
                // Handle the selected color
                selectedColor = color
                btnChooseColour.setBackgroundColor(selectedColor)
                return true // Dialog result was handled
            }
        }
        return false
    }

    fun bool createCategory(){
        //here
        return true
    }


}