package com.example.ailawyer.dataclasses

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.ailawyer.databinding.ActivityAddReportBinding
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


import android.os.Parcel
import android.os.Parcelable
import com.example.ailawyer.AddComplaintsActivity
import com.example.ailawyer.R


class add_report : AppCompatActivity() {
    private lateinit var binding: ActivityAddReportBinding
    private lateinit var complaintsContainer: LinearLayout
    private lateinit var sharedPreferences: SharedPreferences
    private val complaintsList = ArrayList<Complaint>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddReportBinding.inflate(layoutInflater)
        setContentView(binding.root)

        complaintsContainer = binding.complaintsContainer
        sharedPreferences = getSharedPreferences("complaints_prefs", MODE_PRIVATE)

        // Load stored complaints
        loadComplaints()

        binding.addNewReportButton.setOnClickListener {
            startActivity(Intent(this, AddComplaintsActivity::class.java))
        }

        // Check if a new complaint was added
        val newComplaint = intent.getParcelableExtra<Complaint>("complaintData")
        if (newComplaint != null) {
            complaintsList.add(newComplaint) // Add new complaint to list
            saveComplaints() // Save updated list
            displayAllComplaints() // Refresh UI
        }
    }

    private fun displayAllComplaints() {
        complaintsContainer.removeAllViews() // Clear previous views
        for (complaint in complaintsList) {
            displayComplaint(complaint)
        }
    }

    private fun displayComplaint(complaint: Complaint) {
        val complaintCardView = LayoutInflater.from(this).inflate(R.layout.complaint_item, complaintsContainer, false)

        val titleTextView: TextView = complaintCardView.findViewById(R.id.complaintTitle)
        val detailsTextView: TextView = complaintCardView.findViewById(R.id.complaintDetails)
        val stateTextView: TextView = complaintCardView.findViewById(R.id.complaintState)
        val progressTextView: TextView = complaintCardView.findViewById(R.id.complaintProgress)

        titleTextView.text = complaint.title
        detailsTextView.text = complaint.details
        stateTextView.text = complaint.state
        progressTextView.text = complaint.progress

        complaintsContainer.addView(complaintCardView)
    }

    private fun saveComplaints() {
        val editor = sharedPreferences.edit()
        val gson = Gson()
        val json = gson.toJson(complaintsList) // Convert list to JSON string
        editor.putString("complaints_list", json)
        editor.apply()
    }

    private fun loadComplaints() {
        val gson = Gson()
        val json = sharedPreferences.getString("complaints_list", null)
        val type = object : TypeToken<ArrayList<Complaint>>() {}.type
        val storedComplaints: ArrayList<Complaint>? = gson.fromJson(json, type)

        if (storedComplaints != null) {
            complaintsList.addAll(storedComplaints)
        }

        displayAllComplaints()
    }
}
