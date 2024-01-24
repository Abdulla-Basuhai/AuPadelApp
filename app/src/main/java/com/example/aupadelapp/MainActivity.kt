package com.example.aupadelapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.aupadelapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Inflate the layout using the ViewBinding
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Find the NavHostFragment (container) in the layout (where the fragments will be hosted)
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragment_container) as NavHostFragment
        // Get the NavController associated with this NavHostFragment
        navController = navHostFragment.findNavController()

        // Set the toolbar as the action bar for this activity
        setSupportActionBar(binding.toolbar)
        // Set up ActionBar for NavController to handle back button and title updates
        setupActionBarWithNavController(navController)

        // Observe the current destination and update the toolbar visibility accordingly
        navController.addOnDestinationChangedListener { _, destination, _ ->
            // Check if the current fragment is the one where you want to hide the toolbar
            when (destination.id) {
                R.id.entryFragment -> {
                    // Hide the toolbar if the current fragment is entry fragment
                    supportActionBar?.hide()
                }
                else -> {
                    // Show the toolbar for other fragments
                    supportActionBar?.show()
                }
            }
        }
    }
    // Handle the up navigation (back button)
    override fun onSupportNavigateUp(): Boolean {
        // Navigate up in the navigation hierarchy or perform the default action
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}

