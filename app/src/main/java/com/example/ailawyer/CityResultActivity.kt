package com.example.ailawyer

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ailawyer.databinding.ActivityCityResultBinding
import com.example.ailawyer.databinding.LawyerAttributeBinding
import com.google.android.material.navigation.NavigationView

class CityResultActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCityResultBinding
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 1. Set up RecyclerView
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        val lawyerList = List(10) { index -> "Lawyer #${index + 1}" }
        binding.recyclerView.adapter = LawyerAdapter(lawyerList)

        // 2. Optional: onClick for the entire RecyclerView (usually you'd set item-level clicks)
        binding.recyclerView.setOnClickListener {
            startActivity(Intent(this@CityResultActivity, IntroActivity::class.java))
        }

        // 3. DrawerLayout and NavigationView references
        drawerLayout = binding.drawerLayout
        navigationView = binding.navigationView

        // 4. 'Home' icon opens the drawer
        val homeIcon: ImageView = binding.home
        homeIcon.setOnClickListener {
            drawerLayout.openDrawer(GravityCompat.START)
        }

        // 5. Handle navigation drawer item clicks
        navigationView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_setting -> {
                    startActivity(Intent(this, Settings::class.java))
                }
                R.id.nav_chat -> {
                    // Handle chat click
                }
                R.id.nav_cross -> {
                    // Handle cross click
                }
                R.id.nav_location -> {
                    startActivity(Intent(this, CityActivity::class.java))
                }
            }
            // Close drawer after selection
            drawerLayout.closeDrawer(GravityCompat.START)
            true
        }
    }

    // Close the drawer on back press if it's open
    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    // Inner adapter class using binding for each item
    inner class LawyerAdapter(private val lawyers: List<String>) :
        RecyclerView.Adapter<LawyerAdapter.LawyerViewHolder>() {

        inner class LawyerViewHolder(val binding: LawyerAttributeBinding) :
            RecyclerView.ViewHolder(binding.root)

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LawyerViewHolder {
            val itemBinding = LawyerAttributeBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
            return LawyerViewHolder(itemBinding)
        }

        override fun onBindViewHolder(holder: LawyerViewHolder, position: Int) {
            // Bind the lawyer name to the userName TextView
            holder.binding.userName.text = lawyers[position]
        }

        override fun getItemCount(): Int = lawyers.size
    }
}
