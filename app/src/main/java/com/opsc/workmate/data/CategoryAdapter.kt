package com.opsc.workmate.data

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.opsc.workmate.R

class CategoryAdapter(private val categories: List<Category>) :
    RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {

    // ViewHolder class for caching view references
    inner class CategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val categoryNameTextView: TextView = itemView.findViewById(R.id.txtCategoryName)
        val categoryImageView: ImageView = itemView.findViewById(R.id.imgCategoryHasImage)
        val categoryViewButton: Button = itemView.findViewById(R.id.btnViewCategory)
    }

    // Create ViewHolder by inflating the item layout
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        // Inflate the item layout for the ViewHolder
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_category_item, parent, false)
        return CategoryViewHolder(itemView)
    }

    // Bind data to ViewHolder views
    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val currentCategory = categories[position]
        // Set the category name in the corresponding TextView
        holder.categoryNameTextView.text = currentCategory.name

        // Check if imageData is null
        if (currentCategory.imageData != "null") {
            // If imageData is not null, make the ImageView visible
            holder.categoryImageView.visibility = View.VISIBLE
        } else {
            // If imageData is null, make the ImageView invisible
            holder.categoryImageView.visibility = View.INVISIBLE
        }

        val colour = currentCategory.colour ?: Color.WHITE // Use Color.WHITE as default color if null
        holder.categoryViewButton.setBackgroundColor(colour)
    }

    // Return the number of items in the list
    override fun getItemCount() = categories.size
}