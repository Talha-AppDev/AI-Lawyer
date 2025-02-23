package com.example.ailawyer

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.example.ailawyer.databinding.ActivityAddReportBinding

// Data class for Complaint
data class Complaint(val title: String, val details: String, val state: String, val progress: String)

class add_report : AppCompatActivity() {

    private lateinit var binding: ActivityAddReportBinding
    private lateinit var complaintsContainer: LinearLayout
    private val complaintsList = mutableListOf<Complaint>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAddReportBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)

        complaintsContainer = binding.complaintsContainer // Initialize using View Binding

        binding.addNewReportButton.setOnClickListener {
            startActivity(Intent(this, ADD_COMPLAINTS::class.java))
        }

        // Sample Data (Replace with your actual data)
        complaintsList.add(Complaint("Payment Stuck", "23 Oct, 200", "Regular", "Pending"))
        complaintsList.add(Complaint("AI not responding", "1 Nov, 2024", "Regular", "Pending"))

        displayComplaints()

        binding.addNewReportButton.setOnClickListener {
            val newComplaint = Complaint("New Complaint", "Details", "State", "Progress") // Get data
            complaintsList.add(newComplaint)
            displayComplaints()
        }
    }

    @SuppressLint("MissingInflatedId")
    private fun displayComplaints() {
        complaintsContainer.removeAllViews()

        for (complaint in complaintsList) {
            val complaintView = LayoutInflater.from(this).inflate(R.layout.complaint_item, complaintsContainer, false)

            val complaintTitle = complaintView.findViewById<TextView>(R.id.complaintTitle) ?: return // Handle null
            val closeButton = complaintView.findViewById<ImageView>(R.id.closeButton) ?: return // Handle null
            val complaintDetails = complaintView.findViewById<TextView>(R.id.complaintDetails) ?: return
            val complaintState = complaintView.findViewById<TextView>(R.id.complaintState) ?: return
            val complaintProgress = complaintView.findViewById<TextView>(R.id.complaintProgress) ?: return
            val complaintCard = complaintView.findViewById<CardView>(R.id.complaintCard) ?: return


            complaintTitle.text = complaint.title
            complaintDetails.text = complaint.details
            complaintState.text = complaint.state
            complaintProgress.text = complaint.progress

            closeButton.setOnClickListener {
                complaintsList.remove(complaint)
                displayComplaints()
            }

            // Set background color
            when (complaint.state) {
                "Pending" -> complaintCard.setCardBackgroundColor(getColor(android.R.color.holo_orange_light))
                "Resolved" -> complaintCard.setCardBackgroundColor(getColor(android.R.color.holo_green_light))
                else -> complaintCard.setCardBackgroundColor(getColor(android.R.color.white))
            }

            complaintsContainer.addView(complaintView)
        }
    }
}