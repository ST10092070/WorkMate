package com.opsc.workmate.data

import android.content.Context
import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.FrameLayout
import android.widget.TextView
import com.opsc.workmate.R

class ColorAdapter(context: Context, private val colors: IntArray, private val icons: IntArray, private val amounts: Array<String>) : ArrayAdapter<String>(context, 0, amounts) {


    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        // Get the data item for this position
        val color = colors[position]
        val amount = amounts[position]
        // Check if an existing view is being reused, otherwise inflate the view
        var view = convertView
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.grid_layout, parent, false)
        }
        // Lookup view for data population
        val circleColor = view!!.findViewById<FrameLayout>(R.id.cirle)
        val amountText = view.findViewById<TextView>(R.id.amount_text)

        // Populate the data into the template view using the data object
        circleColor.backgroundTintList = ColorStateList.valueOf(color)
        amountText.text = amount

        // Return the completed view to render on screen
        return view
    }

}
