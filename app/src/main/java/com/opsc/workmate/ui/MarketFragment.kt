package com.opsc.workmate.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Button
import android.widget.GridView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.opsc.workmate.R
import com.opsc.workmate.data.ColorItem
import com.opsc.workmate.data.DataManager
import com.opsc.workmate.data.Global
import com.opsc.workmate.data.ColorAdapter
import com.opsc.workmate.data.PhotoAdapter
import com.opsc.workmate.data.NFTItem

class MarketFragment : Fragment(), AdapterView.OnItemClickListener  {
    private var auth: FirebaseAuth = FirebaseAuth.getInstance()
    private lateinit var work_coins: TextView
    private lateinit var grid_color: List<ColorItem>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_market, container, false)

        // Find the grid view by id
        val gridView = view.findViewById<GridView>(R.id.grid_view)
        gridView.onItemClickListener = this

        // Create an array of colors for the circular frame layouts
        val colors = intArrayOf(
            R.color.red,
            R.color.orange,
            R.color.yellow,
            R.color.green,
            R.color.blue,
            R.color.indigo,
            R.color.violet,
            R.color.pink,
            R.color.brown
        )

        // Create an array of icons for the image views
        val icons = intArrayOf(
            R.drawable.wcoins,
            R.drawable.wcoins,
            R.drawable.wcoins,
            R.drawable.wcoins,
            R.drawable.wcoins,
            R.drawable.wcoins,
            R.drawable.wcoins,
            R.drawable.wcoins,
            R.drawable.wcoins
        )

        // Create an array of amounts for the text views
        val amounts = arrayOf(
            "48",
            "34",
            "10",
            "21",
            "25",
            "157",
            "120",
            "35",
            "86"
        )

        val NFTs = listOf(
            NFTItem(R.drawable.nft1, R.drawable.wcoins, 643),
            NFTItem(R.drawable.nft2, R.drawable.wcoins, 498),
            NFTItem(R.drawable.nft3, R.drawable.wcoins, 954),
            NFTItem(R.drawable.nft4, R.drawable.wcoins, 834),
            NFTItem(R.drawable.nft5, R.drawable.wcoins, 1760),
            NFTItem(R.drawable.nft6, R.drawable.wcoins, 503),
            NFTItem(R.drawable.nft7, R.drawable.wcoins, 1980),
            NFTItem(R.drawable.nft8, R.drawable.wcoins, 410),
            NFTItem(R.drawable.nft9, R.drawable.wcoins, 343)
        )

        grid_color = listOf(
            ColorItem(R.color.red, R.drawable.wcoins, 48),
            ColorItem(R.color.orange, R.drawable.wcoins, 34),
            ColorItem(R.color.yellow, R.drawable.wcoins, 10),
            ColorItem(R.color.green, R.drawable.wcoins, 21),
            ColorItem(R.color.blue, R.drawable.wcoins, 25),
            ColorItem(R.color.indigo, R.drawable.wcoins, 157),
            ColorItem(R.color.violet, R.drawable.wcoins, 120),
            ColorItem(R.color.pink, R.drawable.wcoins, 35),
            ColorItem(R.color.brown, R.drawable.wcoins, 86)

        )

        val recyclerView: RecyclerView = view.findViewById(R.id.recyclerView)
        val layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        recyclerView.layoutManager = layoutManager

        val photoAdapter = PhotoAdapter(NFTs)
        recyclerView.adapter = photoAdapter

        // Create an adapter with the colors, icons and amounts arrays
        val colorAdapter = ColorAdapter(requireContext(), colors, icons, amounts)

        // Set the adapter to the grid view
        gridView.adapter = colorAdapter

        work_coins = view.findViewById(R.id.txtWorkCoins)

        val btn10 = view.findViewById<Button>(R.id.btnR10)
        val btn25 = view.findViewById<Button>(R.id.btnR25)
        val btn50 = view.findViewById<Button>(R.id.btnR50)
        val btn100 = view.findViewById<Button>(R.id.btnR100)

        btn10.setOnClickListener {
            Toast.makeText(context, "Thank you, Your donation of R10 has been received.", Toast.LENGTH_SHORT).show()
        }
        btn25.setOnClickListener {
            Toast.makeText(context, "Thank you, Your donation of R25 has been received.", Toast.LENGTH_SHORT).show()
        }
        btn50.setOnClickListener {
            Toast.makeText(context, "Thank you, Your donation of R50 has been received.", Toast.LENGTH_SHORT).show()
        }
        btn100.setOnClickListener {
            Toast.makeText(context, "Thank you, Your donation of R100 has been received.", Toast.LENGTH_SHORT).show()
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        try{
            DataManager.getWorkcoins(Global.currentUser!!.uid.toString()){ coins ->
                work_coins.text = StringBuilder().append(coins).toString()
            }
        }catch (e: Exception){
            Log.d("coins exception",e.message.toString())
        }
    }

    var isDeductionMade = false // Flag variable to track deduction

    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        // Handle item click event
        val selectedItem = grid_color[position]
        isDeductionMade = false
        try {
            DataManager.getWorkcoins(Global.currentUser!!.uid.toString()) { coins ->
                val userCoins = coins.toString().toInt()
                val colorPurchaseAmount = selectedItem.currency

                if (!isDeductionMade) {
                    if (userCoins < colorPurchaseAmount) {
                        val difference = colorPurchaseAmount - userCoins
                        Toast.makeText(
                            requireContext(),
                            "Insufficient funds. You need ₩ $difference to make this purchase!",
                            Toast.LENGTH_SHORT
                        ).show()
                        Toast.makeText(
                            requireContext(),
                            "Try doing more activities to earn more coins, keep working :)",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        // Deduct the amount of the purchased color
                        val deduction = userCoins - colorPurchaseAmount
                        DataManager.setWorkcoins(deduction) { isSuccess ->
                            if (isSuccess) {
                                isDeductionMade = true // Set the flag to indicate deduction is made
                                Toast.makeText(
                                    activity,
                                    "Purchase successful, ₩ $colorPurchaseAmount coins were deducted from your account, keep ₩orking :)",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }
                }
            }
        } catch (e: Exception) {
            Log.d("coins exception", e.message.toString())
        }
    }

}