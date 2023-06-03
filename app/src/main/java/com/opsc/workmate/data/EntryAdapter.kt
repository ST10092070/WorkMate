package com.opsc.workmate.data

import android.graphics.Color
import android.media.Image
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.opsc.workmate.R


//class com.opsc.workmate.data.EntryAdapter(
//    private val entries: List<Entry>,
//    private val currentUsername: String
//) : RecyclerView.Adapter<com.opsc.workmate.data.EntryAdapter.EntryViewHolder>() {
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EntryViewHolder {
//        val view = LayoutInflater.from(parent.context)
//            .inflate(R.layout.list_entry_item, parent, false)
//        return EntryViewHolder(view)
//    }
//
//    @RequiresApi(Build.VERSION_CODES.O)
//    override fun onBindViewHolder(holder: EntryViewHolder, position: Int) {
//        val entry = entries[position]
//        if (entry.username == currentUsername) {
//            holder.bind(entry)
//        } else {
//            holder.hide()
//        }
//    }
//
//    override fun getItemCount(): Int {
//        return entries.size
//    }
//
//    inner class EntryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//        private val txtEntryDate: TextView = itemView.findViewById(R.id.txtEntryDate)
//        private val txtStartEndTime: TextView = itemView.findViewById(R.id.txtStartEndTime)
//        private val txtDuration: TextView = itemView.findViewById(R.id.txtDuration)
//        private val imgHasImage: ImageView = itemView.findViewById(R.id.imgHasImage)
//
//        @RequiresApi(Build.VERSION_CODES.O)
//        fun bind(entry: Entry) {
//            txtEntryDate.text = entry.date
//            txtStartEndTime.text = "${entry.startTime} -> ${entry.endTime}"
//
//            val startTime = LocalTime.parse(entry.startTime)
//            val endTime = LocalTime.parse(entry.endTime)
//            val duration = endTime.minusHours(startTime.hour.toLong())
//                .minusMinutes(startTime.minute.toLong())
//            txtDuration.text = "${duration.hour}:${duration.minute} hrs"
//
//            if (entry.imageData != null) {
//                imgHasImage.visibility = View.VISIBLE
//            } else {
//                imgHasImage.visibility = View.INVISIBLE
//            }
//        }
//
//        fun hide() {
//            itemView.visibility = View.GONE
//        }
//    }
//}

//class EntryAdapter(private val entries: List<Entry>) :
//    RecyclerView.Adapter<EntryAdapter.EntryViewHolder>() {
//
//    // ViewHolder class for caching view references
//    inner class EntryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//        val entryDateTextView: TextView = itemView.findViewById(R.id.txtEntryDate)
//        val startEndDateTextView: TextView = itemView.findViewById(R.id.txtStartEndTime)
//        val durationTextView: TextView = itemView.findViewById(R.id.txtDuration)
//        val hasImageImageView: ImageView = itemView.findViewById(R.id.imgHasImage)
//    }
//
//    // Create ViewHolder by inflating the item layout
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EntryViewHolder {
//        // Inflate the item layout for the ViewHolder
//        val itemView = LayoutInflater.from(parent.context)
//            .inflate(R.layout.list_entry_item, parent, false)
//        return EntryViewHolder(itemView)
//    }
//
//    // Bind data to ViewHolder views
//    override fun onBindViewHolder(holder: EntryViewHolder, position: Int) {
//        val currentEntry = entries[position]
//
//        //Set the entry date
//        holder.entryDateTextView.text = currentEntry.date
//        //Set the entry start end times
//        holder.startEndDateTextView.text = currentEntry.startTime + " -> " + currentEntry.endTime
//        //Set the duration
//        holder.durationTextView.text = ""
//
//        //Check imageData
//        if (currentEntry.imageData != "null") {
//            holder.hasImageImageView.visibility = View.VISIBLE
//        } else {
//            holder.hasImageImageView.visibility = View.VISIBLE
//        }
//    }
//
//    // Return the number of items in the list
//    override fun getItemCount() = entries.size
//}

class EntryAdapter(private val entries: List<Entry>) :
    RecyclerView.Adapter<EntryAdapter.EntryViewHolder>() {

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
    }
}
