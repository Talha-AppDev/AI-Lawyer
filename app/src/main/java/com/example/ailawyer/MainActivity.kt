package com.example.ailawyer

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.provider.Settings
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import com.example.ailawyer.databinding.ActivityMainBinding
import com.example.ailawyer.fragMA.ViewPagerAdapter
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.InetSocketAddress
import java.net.Socket

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    // We'll keep these as properties so they are available in the click listener.
    private val auth = FirebaseAuth.getInstance()
    private val currentUser get() = auth.currentUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Remove the direct redirect code. Instead, let the tabs load for all users.
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sharedPref = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        val userType = sharedPref.getString("userType", null)

        val viewPager = binding.viewPager
        val tabLayout = binding.tabLayout
        viewPager.adapter = ViewPagerAdapter(this)

        // Attach tabs with the view pager
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> "Tab One"
                1 -> "Tab Two"
                2 -> "Tab Three"
                else -> null
            }
        }.attach()

        // Listen for page changes so that on the last tab we show the "Continue" button.
        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                if (position == 2) {
                    binding.pg.text = "Continue"
                    binding.pg.setOnClickListener {
                        // Launch a coroutine to check for active internet connection asynchronously.
                        lifecycleScope.launch {
                            if (hasActiveInternet()) {
                                // Check if auth and shared preferences are valid.
                                // If both are valid, then go to CityActivity.
                                if (currentUser != null && !userType.isNullOrEmpty()) {
                                    startActivity(Intent(this@MainActivity, CityActivity::class.java))
                                } else {
                                    // Otherwise, proceed as usual to TypeActivity.
                                    startActivity(Intent(this@MainActivity, TypeActivity::class.java))
                                }
                            } else {
                                showInternetDialog()
                            }
                        }
                    }
                } else {
                    binding.pg.text = "Swipe Left"
                    binding.pg.setOnClickListener(null) // Clear listener for other pages.
                }
            }
        })
    }

    // This function checks for active internet access using a socket connection.
    private suspend fun hasActiveInternet(): Boolean = withContext(Dispatchers.IO) {
        try {
            Socket().use { socket ->
                // Connect to a reliable server (Google DNS) on port 53
                socket.connect(InetSocketAddress("8.8.8.8", 53), 1500)
            }
            true
        } catch (e: Exception) {
            false
        }
    }

    // Display an AlertDialog if there's no internet connection.
    private fun showInternetDialog() {
        AlertDialog.Builder(this)
            .setTitle("No Internet Connection")
            .setMessage("Please connect to the internet to continue.")
            .setPositiveButton("Wi-Fi Settings") { _, _ ->
                startActivity(Intent(Settings.ACTION_WIFI_SETTINGS))
            }
            .setNegativeButton("Mobile Data Settings") { _, _ ->
                startActivity(Intent(Settings.ACTION_DATA_ROAMING_SETTINGS))
            }
            .setNeutralButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }
}
