package com.example.ailawyer

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ailawyer.databinding.ActivityLawyerScreenBinding

class LawyerScreenActivity : AppCompatActivity() {

    // Data Model
    data class Lawyer(
        val name: String,
        val reviews: String,
        val imageRes: Int
    )

    private lateinit var binding: ActivityLawyerScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLawyerScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Button to open AiChatActivity (if needed)
        binding.btnAiAssistant.setOnClickListener {
            startActivity(Intent(this, AiChatActivity::class.java))
        }

        binding.btnBack.setOnClickListener {
            startActivity(Intent(this, CityActivity::class.java))
        }

        // Set up RecyclerView with a LayoutManager
        val recyclerView: RecyclerView = binding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Create a list of lawyers
        val lawyerList = listOf(
            Lawyer("Asma #1", "4.8 out of 30 reviews", R.drawable.ic),
            Lawyer("Asma #2", "4.5 out of 22 reviews", R.drawable.ic),
            Lawyer("Asma #3", "4.9 out of 50 reviews", R.drawable.ic),
            Lawyer("Asma #4", "4.6 out of 18 reviews", R.drawable.ic)
        )

        // Set the adapter
        recyclerView.adapter = LawyerAdapter(lawyerList)
    }

    // RecyclerView Adapter inflating lawyer_attribute.xml
    inner class LawyerAdapter(private val lawyers: List<Lawyer>) :
        RecyclerView.Adapter<LawyerAdapter.LawyerViewHolder>() {

        // ViewHolder
        inner class LawyerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val profileImage: ImageView = itemView.findViewById(R.id.profileImage)
            val userName: TextView = itemView.findViewById(R.id.userName)
            val reviews: TextView = itemView.findViewById(R.id.reviews)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LawyerViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.lawyer_attribute, parent, false)
            return LawyerViewHolder(view)
        }

        override fun onBindViewHolder(holder: LawyerViewHolder, position: Int) {
            val lawyer = lawyers[position]
            holder.userName.text = lawyer.name
            holder.reviews.text = lawyer.reviews
            holder.profileImage.setImageResource(lawyer.imageRes)

            // Set click listener on each item to navigate to AiChatActivity
            holder.itemView.setOnClickListener {
                val intent = Intent(this@LawyerScreenActivity, ChatscreenActivity::class.java)
                this@LawyerScreenActivity.startActivity(intent)
            }
        }

        override fun getItemCount(): Int = lawyers.size
    }
}
