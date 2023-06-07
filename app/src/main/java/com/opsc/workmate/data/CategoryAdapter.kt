package com.opsc.workmate.data

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.opsc.workmate.R
import com.opsc.workmate.data.Global.categories
import java.time.Duration
import java.time.LocalTime
import java.time.format.DateTimeFormatter

class CategoryAdapter(
    private val categories: List<Category>,
    private val onItemClickListener: OnItemClickListener
) :
    RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {

    inner class CategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val categoryNameTextView: TextView = itemView.findViewById(R.id.txtCategoryName)
        val categoryImageView: ImageView = itemView.findViewById(R.id.imgCategoryListHasImage)
        val categoryViewButton: Button = itemView.findViewById(R.id.btnViewCategory)
        val categoryDurationTextView: TextView = itemView.findViewById(R.id.txtCategoryduration)
        init {
            categoryViewButton.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val clickedCategory = categories[position]
                    onItemClickListener.onItemClick(clickedCategory)
                }
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(category: Category)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_category_item, parent, false)
        return CategoryViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val currentCategory = categories[position]
        holder.categoryNameTextView.text = currentCategory.name

        if (currentCategory.imageData == null || currentCategory.imageData == "null") {
            holder.categoryImageView.visibility = View.INVISIBLE
        }

        val colour = currentCategory.colour ?: Color.WHITE
        holder.categoryViewButton.setBackgroundColor(colour)

        //Calculate category total duration spent
        //First get list of only this category's entries
        var entries = Global.entries
        val filteredEntries: MutableList<Entry> = mutableListOf()
        entries.forEach { entry ->
            if (entry.categoryName.equals(currentCategory.name, ignoreCase = true))
                filteredEntries.add(entry)
        }

        //Calculate time and display
        val timeSpent = calculateTotalTimeSpent(filteredEntries)
        val hours = timeSpent / 60
        val minutes = timeSpent % 60
        val timeSpentString = String.format("%02d:%02d hrs", hours, minutes)

        holder.categoryDurationTextView.text = timeSpentString

    }

    override fun getItemCount() = categories.size

    private fun calculateTotalTimeSpent(entries: List<Entry>): Long {
        var totalTimeSpent: Long = 0

        for (entry in entries) {
            val startTime = LocalTime.parse(entry.startTime, DateTimeFormatter.ofPattern("hh:mm a"))
            val endTime = LocalTime.parse(entry.endTime, DateTimeFormatter.ofPattern("hh:mm a"))
            val duration = Duration.between(startTime, endTime).toMinutes()
            totalTimeSpent += duration
        }

        return totalTimeSpent
    }
}
