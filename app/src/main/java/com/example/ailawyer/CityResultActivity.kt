package com.example.ailawyer

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ailawyer.databinding.ActivityCityResultBinding
import com.example.ailawyer.databinding.LawyerAttributeBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class CityResultActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCityResultBinding
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var bottomNavigationView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set up RecyclerView with LinearLayoutManager and adapter
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(this@CityResultActivity)
            adapter = LawyerAdapter(generateLawyerList(10))
        }

        // Initialize DrawerLayout and BottomNavigationView from binding
        drawerLayout = binding.drawerLayout
        bottomNavigationView = binding.bottomNavigationView

        // Handle bottom navigation item clicks
        bottomNavigationView.setOnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.navigation_setting -> startActivity(Intent(this, Settings::class.java))
                R.id.navigation_Chats -> startActivity(Intent(this, LawyerScreenActivity::class.java))
                R.id.navigation_location -> startActivity(Intent(this, CityActivity::class.java))
            }
            // Close the drawer after selection if open
            if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                drawerLayout.closeDrawer(GravityCompat.START)
            }
            true
        }
    }

    /**
     * Generates a list of lawyer names.
     */
    private fun generateLawyerList(count: Int): List<String> =
        List(count) { index -> "Lawyer #${index + 1}" }

    /**
     * Close the drawer on back press if it's open.
     */
    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    /**
     * RecyclerView Adapter for displaying a list of lawyers.
     */
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

            // Set an onClickListener on the item view to navigate to IntroActivity
            holder.itemView.setOnClickListener {
                val intent = Intent(this@CityResultActivity, IntroActivity::class.java)
                startActivity(intent)
            }
        }

        override fun getItemCount(): Int = lawyers.size
    }
}
