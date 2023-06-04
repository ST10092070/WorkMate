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

class CategoryAdapter(
    private val categories: List<Category>,
    private val onItemClickListener: OnItemClickListener
) :
    RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {

    inner class CategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val categoryNameTextView: TextView = itemView.findViewById(R.id.txtCategoryName)
        val categoryImageView: ImageView = itemView.findViewById(R.id.imgCategoryHasImage)
        val categoryViewButton: Button = itemView.findViewById(R.id.btnViewCategory)

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

        if (currentCategory.imageData != null || currentCategory.imageData != "null") {
            holder.categoryImageView.visibility = View.VISIBLE
        } else {
            holder.categoryImageView.visibility = View.INVISIBLE
        }

        val colour = currentCategory.colour ?: Color.WHITE
        holder.categoryViewButton.setBackgroundColor(colour)
    }

    override fun getItemCount() = categories.size
}
