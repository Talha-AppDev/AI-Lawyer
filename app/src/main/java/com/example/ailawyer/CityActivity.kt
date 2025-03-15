package com.example.ailawyer

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.ailawyer.databinding.ActivityCityBinding
import com.google.android.material.tabs.TabLayout
import androidx.fragment.app.FragmentTransaction
import com.example.ailawyer.typeFrag.SelectcityFragment

class CityActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityCityBinding.inflate(layoutInflater)

        setContentView(binding.root)



        // Set the first fragment by default when the activity is launched
        if (savedInstanceState == null) {
            // Load Tab One fragment as default
            loadFragment(SelectcityFragment())
        }

        // Set up TabLayout and Tab click listener
        val tabLayout = binding.tabLayout
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when (tab?.position) {
                    0 -> loadFragment(SelectcityFragment())
                }
            }
            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })
    }

    // Helper function to load fragments
    private fun loadFragment(fragment: androidx.fragment.app.Fragment) {
        val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container_view, fragment) // Use the container ID to replace fragments
        transaction.addToBackStack(null) // Add to back stack if you want to allow back navigation
        transaction.commit()
    }
}
