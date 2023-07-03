package com.opsc.workmate.data

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.opsc.workmate.R

class PhotoAdapter(private val photoList: List<NFTItem>) :
    RecyclerView.Adapter<PhotoAdapter.PhotoViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_photo, parent, false)
        return PhotoViewHolder(view)
    }

    override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) {
        val photoItem = photoList[position]
        holder.NFT.setImageResource(photoItem.nft)
        holder.icon.setImageResource(photoItem.coinIcon)
        holder.amount.text = photoItem.amount
    }

    override fun getItemCount(): Int {
        return photoList.size
    }

    class PhotoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val NFT: ImageView = itemView.findViewById(R.id.imgNFT)
        val icon: ImageView = itemView.findViewById(R.id.imgCoin_icon)
        val amount: TextView = itemView.findViewById(R.id.txtAmount)
    }
}

