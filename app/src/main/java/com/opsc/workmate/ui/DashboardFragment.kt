package com.opsc.workmate.ui

import android.content.ContentValues
import android.content.Intent
import android.media.Image
import com.opsc.workmate.data.EntryAdapter
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.Query
import com.google.firebase.database.ValueEventListener
import com.opsc.workmate.R
import com.opsc.workmate.data.Category
import com.opsc.workmate.data.CategoryAdapter
import com.opsc.workmate.data.Entry
import com.opsc.workmate.data.Global
import com.opsc.workmate.data.Global.entries
import com.opsc.workmate.data.Global.users


class DashboardFragment : Fragment(), EntryAdapter.OnItemClickListener, CategoryAdapter.OnItemClickListener {
    private var auth: FirebaseAuth = FirebaseAuth.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_dashboard, container, false)

        var work_coins: TextView = view.findViewById(R.id.txtWorkCoins)

        for(user in users){
            //get the user reference in firebase
            val reference = FirebaseDatabase.getInstance().getReference("User")
            //check if there is any user with the entered user name
            val checkUser: Query = reference.orderByChild("name")
                .equalTo(user.username)

            checkUser.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if(snapshot.exists()){
                        val fb_name = snapshot.child(user.username).child("name").getValue(String::class.java)
                        val fb_email = snapshot.child(user.username).child("email").getValue(String::class.java)
                        val fb_password = snapshot.child(user.username).child("password").getValue(String::class.java)

                        //check if the user doesn't exists
                        if(user.username.equals(fb_name) || user.password.equals(fb_password)){
                            if (fb_email != null) {
                                //try to sign the user in
                                try{
                                    if (fb_email != null) {
                                        //display the user work coins
                                        work_coins.text = user.workcoins.toString()

                                    }
                                }catch (ex: Exception){
                                    Toast.makeText(
                                        context,
                                        ex.message,
                                        Toast.LENGTH_SHORT,
                                    ).show()
                                }
                            }
                        }else{

                        }
                    }
                }
                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(
                        context,
                        "Cancelled",
                        Toast.LENGTH_SHORT,
                    ).show()
                }
            })

        }


        //implement RecyclerView for Categories -------------
        // Find the RecyclerView by ID
        val lstCategories: RecyclerView = view.findViewById(R.id.lstCategories)

        // Create and set the CategoryAdapter with the Global.categories list
        //Moved to onViewCreated to add onclicklistener

        // Create and set the EntryAdapter with the Global.entries list
        //Moved to onViewCreated to add onclicklistener

        //---- Navigate to Create Category screen ----
        // Find the btn by ID
        val btnCreate: Button = view.findViewById(R.id.btnCreate)

        // Set onClickListener for the TextView
        btnCreate.setOnClickListener {
            // Get the NavController
            val navController = Navigation.findNavController(view)

            // Navigate to the CreateCategoryFragment
            navController.navigate(R.id.action_dashboardFragment_to_createCategoryFragment)
        }

        //---- Navigate to Add Entry screen ----
        // Find the btn by ID
        val btnAddEntry: Button = view.findViewById(R.id.btnEntry)

        // Set onClickListener for the TextView
        btnAddEntry.setOnClickListener {
            // Get the NavController
            val navController = Navigation.findNavController(view)

            // Navigate to the CreateCategoryFragment
            navController.navigate(R.id.action_dashboardFragment_to_newEntryFragment)
        }

        auth = FirebaseAuth.getInstance()
        // Find the btn by ID
        val btnSignout: Button = view.findViewById(R.id.btnSignout)
        btnSignout.setOnClickListener {

            //signing the current user out of firebase
            auth.signOut()

            Toast.makeText(context,"Signing out...", Toast.LENGTH_SHORT).show()
            //navigating to the login fragment
            replaceFragment(LoginFragment())
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val lstEntries: RecyclerView = view.findViewById(R.id.lstEntries)

        // Set up the LinearLayoutManager for the RecyclerView
        val entryLayoutManager = LinearLayoutManager(requireContext())
        lstEntries.layoutManager = entryLayoutManager

        // Create an instance of EntryAdapter
        val adapter = EntryAdapter(entries)

        // Set the item click listener
        adapter.setOnItemClickListener(this)

        // Set the adapter to the RecyclerView
        lstEntries.adapter = adapter

        val lstCategories: RecyclerView = view.findViewById(R.id.lstCategories)

        // Set up the LinearLayoutManager for the RecyclerView
        val catLayoutManager = LinearLayoutManager(requireContext())
        lstCategories.layoutManager = catLayoutManager

        // Create an instance of CategoryAdapter and pass the OnItemClickListener
        val catAdapter = CategoryAdapter(Global.categories, this)

        // Set the adapter to the RecyclerView
        lstCategories.adapter = catAdapter
    }

    // Implementation of the onItemClick method from the OnItemClickListener interface
    override fun onItemClick(entry: Entry) {
        // Handle the click event and navigate to a different fragment
        //Add data to bundle
        val bundle = Bundle()
        bundle.putString("username", entry.username)
        bundle.putString("category", entry.categoryName)
        bundle.putString("date", entry.date)
        bundle.putString("startTime", entry.startTime)
        bundle.putString("endTime", entry.endTime)
        bundle.putString("imageData", entry.imageData)
        bundle.putString("description", entry.description)

        val fragment = EntryFragment()
        fragment.arguments = bundle

        //Navigate to fragment, passing bundle
        findNavController().navigate(R.id.action_dashboardFragment_to_entryFragment, bundle)
    }

    // Implementation of the onItemClick method from the CategoryAdapter.OnItemClickListener interface
    override fun onItemClick(category: Category) {
        // Handle the click event and navigate to a different fragment
        //Add data to bundle
        val bundle = Bundle()
        bundle.putString("username", category.username)
        bundle.putString("name", category.name)
        category.colour?.let { bundle.putInt("colour", it) }
        bundle.putString("imageData", category.imageData)

        val fragment = CategoryDetailsFragment()
        fragment.arguments = bundle

        //Navigate to fragment, passing bundle
        findNavController().navigate(R.id.action_dashboardFragment_to_categoryDetailsFragment, bundle)
    }

    fun replaceFragment(fragment : Fragment){

        val fragmentManager = requireActivity().supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragmentContainer,fragment)
        fragmentTransaction.commit()
    }
}