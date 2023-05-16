package com.opsc.workmate

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.internal.NavigationMenuView
import com.google.android.material.navigation.NavigationView
import com.opsc.workmate.R


class MainActivity : AppCompatActivity() {

    lateinit var drawerLayout: DrawerLayout
    lateinit var actionBarDrawerToggle: ActionBarDrawerToggle
    lateinit var navView: NavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // drawer layout instance to toggle the menu icon to open
        // drawer and back button to close drawer
        drawerLayout = findViewById(R.id.my_drawer_layout)
        actionBarDrawerToggle = ActionBarDrawerToggle(this, drawerLayout, R.string.nav_open, R.string.nav_close)

        // pass the Open and Close toggle for the drawer layout listener
        // to toggle the button
        drawerLayout.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()

        // to make the Navigation drawer icon always appear on the action bar
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        //

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        navView = findViewById(R.id.navMenu)
        navView.setupWithNavController(navController)

        // Set item click listener for the navigation menu
        navView.setNavigationItemSelectedListener { menuItem ->
            // Handle menu item clicks here
            when (menuItem.itemId) {
                R.id.dashboardFragment -> {
                    // Handle the Dashboard menu item click
                    navController.navigate(R.id.dashboardFragment)
                    drawerLayout.closeDrawers()
                    true
                }
                R.id.createProjectFragment -> {
                    // Handle the Add Category menu item click
                    navController.navigate(R.id.createProjectFragment)
                    drawerLayout.closeDrawers()
                    true
                }
                R.id.newEntryFragment -> {
                    // Handle the Add Entry menu item click
                    navController.navigate(R.id.newEntryFragment)
                    drawerLayout.closeDrawers()
                    true
                }
                else -> false
            }
        }

    }

    // override the onOptionsItemSelected()function to implement the item click listener callback
    // to open and close the navigation drawer when the icon is clicked
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            true
        } else super.onOptionsItemSelected(item)
    }
}