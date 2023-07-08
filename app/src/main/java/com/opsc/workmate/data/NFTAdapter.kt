package com.opsc.workmate.data

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
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

            try{
                DataManager.getWorkcoins(Global.currentUser!!.uid.toString()){ coins ->
                    var user_coins = coins.toString().toInt()
                    var color_purchase_amount = selectedItem.amount
                    if(user_coins < color_purchase_amount){
                        val difference = color_purchase_amount - user_coins
                        Toast.makeText(itemView.context, "Insufficient funds you need ₩ $difference to make this purchase!", Toast.LENGTH_SHORT).show()
                        Toast.makeText(itemView.context, "Try doing more activities to earn more coins, keep ₩orking :)", Toast.LENGTH_SHORT).show()
                    }

                    if(user_coins > color_purchase_amount){
                        //deduct the amount of the purchased color
                        var deduction = user_coins - color_purchase_amount
                        DataManager.setWorkcoins(deduction) { isSuccess ->
                            if (isSuccess){
                                Toast.makeText(itemView.context, "Purchase successful, ₩ $color_purchase_amount coins were deducted from your account, keep ₩orking :)", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }
            }catch (e: Exception){
                Log.d("coins exception",e.message.toString())
            }
        }
    }
}