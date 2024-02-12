package com.example.aupadelapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.aupadelapp.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var appBarConfiguration: AppBarConfiguration
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Inflate the layout using the ViewBinding
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Find the NavHostFragment (container) in the layout (where the fragments will be hosted)
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragment_container) as NavHostFragment
        // Get the NavController associated with this NavHostFragment
        navController = navHostFragment.findNavController()

        // Define top-level destinations for the app bar configuration
        // These destinations do not display a "Up" button in the app bar
        // and are considered as main entry points of the app
        appBarConfiguration = AppBarConfiguration(
            setOf(R.id.homeFragment,R.id.profileFragment,R.id.notificationFragment)
        )

        // Set the toolbar as the action bar for this activity
        setSupportActionBar(binding.toolbar)

        /*
         ## setupActionBarWithNavController(navController):
         Set up ActionBar for NavController to handle back button and title updates.
         This enables handling back button presses and title updates.

         ## setupActionBarWithNavController(navController,appBarConfiguration)
        When specifying the AppBarConfiguration, the action bar will only display a back button for destinations
        that are not part of the top-level destinations defined in the AppBarConfiguration.
         */

        setupActionBarWithNavController(navController,appBarConfiguration)

        // after create menu file and bottomNavigationView, we have to connect the bottomNavigationView with the navGraph using NavController
        // Set up bottom navigation with NavController to handle navigation events
        binding.bottomNavigationView.setupWithNavController(navController)

        // Observe the current destination and update the toolbar visibility accordingly
        navController.addOnDestinationChangedListener { _, destination, _ ->
            // Check if the current fragment is the one where you want to hide the toolbar
            when (destination.id) {
                R.id.entryFragment,R.id.homeFragment -> {
                    // Hide the toolbar if the current fragment is entry fragment
                    supportActionBar?.hide()
                }
                else -> {
                    // Show the toolbar for other fragments
                    supportActionBar?.show()
                }
            }
        }
        // Observe the current destination and update the bottom navigation visibility accordingly
        navController.addOnDestinationChangedListener { _, destination, _ ->
            // Check if the current fragment is the one where you want to hide the bottom navigation
            when (destination.id) {
                R.id.entryFragment,R.id.loginFragment,R.id.registrationFragment,R.id.accountVerificationFragment-> {
                    binding.bottomNavigationView.visibility  = View.GONE
                }
                else -> {
                    // Show the bottom navigation for other fragments
                    binding.bottomNavigationView.visibility  = View.VISIBLE
                }
            }
        }

        firebaseAuth = FirebaseAuth.getInstance()
        // check if the user is not authenticated
        if (firebaseAuth.currentUser == null){
            navController.navigate(R.id.entryFragment)
        }

    }
    // Handle the up navigation (back button)
    override fun onSupportNavigateUp(): Boolean {
        // Navigate up in the navigation hierarchy or perform the default action
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}

