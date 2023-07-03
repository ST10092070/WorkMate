package com.opsc.workmate.data

import android.graphics.Color
import android.media.Image
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.recyclerview.widget.RecyclerView
import com.opsc.workmate.R
import org.w3c.dom.Text

class EntryAdapter(private var entries: List<Entry>) :
    RecyclerView.Adapter<EntryAdapter.EntryViewHolder>() {

    // Interface for click events
    interface OnItemClickListener {
        fun onItemClick(entry: Entry)
    }

    fun setEntries(entries: List<Entry>) {
        this.entries = entries
    }

    private var onItemClickListener: OnItemClickListener? = null

    // Setter for the click listener
    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.onItemClickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EntryViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_entry_item, parent, false)
        return EntryViewHolder(view)
    }

    override fun onBindViewHolder(holder: EntryViewHolder, position: Int) {
        val entry = entries[position]
        holder.bind(entry)
    }

    override fun getItemCount(): Int {
        return entries.size
    }

    inner class EntryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val txtEntryDate: TextView = itemView.findViewById(R.id.txtEntryDate)
        private val txtStartEndTime: TextView = itemView.findViewById(R.id.txtStartEndTime)
        private val txtDuration: TextView = itemView.findViewById(R.id.txtDuration)
        private val imgHasImage: ImageView = itemView.findViewById(R.id.imgHasImage)
        private val txtCatName: TextView = itemView.findViewById(R.id.txtCatName)
        private val entryLinearLayout: LinearLayoutCompat = itemView.findViewById(R.id.entryLinearLayout)

        fun bind(entry: Entry) {
            txtEntryDate.text = entry.date
            txtStartEndTime.text = "${entry.startTime} -> ${entry.endTime}"

            val duration = calculateDuration(entry.startTime, entry.endTime)
            txtDuration.text = duration

            if (entry.imageData != "null") {
                imgHasImage.visibility = View.VISIBLE
            } else {
                imgHasImage.visibility = View.INVISIBLE
            }

            //Set category name
            txtCatName.text = entry.categoryName

            //Set colour of entry to category colour
            var colour = Global.categories.find { it.name.equals(entry.categoryName, ignoreCase = true) }?.colour

            if (colour != null)
                entryLinearLayout.setBackgroundColor(colour)

        }

        private fun calculateDuration(startTime: String, endTime: String): String {
            // Assuming the time format is "hh:mm"
            val startHour = startTime.substring(0, 2).toInt()
            val startMinute = startTime.substring(3, 5).toInt()
            val endHour = endTime.substring(0, 2).toInt()
            val endMinute = endTime.substring(3, 5).toInt()

            val durationHour = endHour - startHour
            val durationMinute = endMinute - startMinute

            return String.format("%02d:%02d hrs", durationHour, durationMinute)
        }

        init {
            // Set click listener for the itemView
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val entry = entries[position]
                    // Call the onItemClick method of the listener with the clicked entry
                    onItemClickListener?.onItemClick(entry)
                }
            }
        }

    }
}
