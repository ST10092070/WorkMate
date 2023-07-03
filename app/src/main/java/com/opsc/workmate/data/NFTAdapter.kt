package com.opsc.workmate.data

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.opsc.workmate.R

class NFTAdapter(private val NFTs: List<NFTItem>) : RecyclerView.Adapter<NFTAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_photo, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = NFTs[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int {
        return NFTs.size
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        private val NFT: ImageView = itemView.findViewById(R.id.imgNFT)
        private val coin: ImageView = itemView.findViewById(R.id.imgCoin_icon)
        private val amount: TextView = itemView.findViewById(R.id.txtAmount)

        init {
            itemView.setOnClickListener(this)
        }

        fun bind(item: NFTItem) {
            NFT.setImageResource(item.nft)
            coin.setImageResource(item.coinIcon)
            amount.text = item.amount.toString()
        }

        override fun onClick(view: View) {
            val position = adapterPosition
            val selectedItem = NFTs[position]
            Toast.makeText(itemView.context, "Amount: ${selectedItem.amount}", Toast.LENGTH_SHORT).show()
        }
    }
}
