package com.example.ailawyer

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.ailawyer.databinding.ActivityAddReportBinding


import android.os.Parcel
import android.os.Parcelable

// Data class for Complaint
data class Complaint(
    var title: String,
    var details: String,
    var state: String,
    var progress: String
) : Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: ""
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(title)
        parcel.writeString(details)
        parcel.writeString(state)
        parcel.writeString(progress)
    }

    override fun describeContents(): Int = 0

    companion object CREATOR : Parcelable.Creator<Complaint> {
        override fun createFromParcel(parcel: Parcel): Complaint {
            return Complaint(parcel)
        }

        override fun newArray(size: Int): Array<Complaint?> {
            return arrayOfNulls(size)
        }
    }
}


class add_report : AppCompatActivity() {

    private lateinit var binding: ActivityAddReportBinding
    private lateinit var complaintsContainer: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddReportBinding.inflate(layoutInflater)
        setContentView(binding.root)

        complaintsContainer = binding.complaintsContainer // Initialize using View Binding
        binding.addNewReportButton.setOnClickListener {
            startActivity(Intent(this, ADD_COMPLAINTS::class.java))
        }

        // Check if the Intent has the complaintData
        val complaint = intent.getParcelableExtra<Complaint>("complaintData")

        if (complaint != null) {
            // Display the received complaint
            displayComplaint(complaint)
        }
    }

    // This function creates and adds a new card for each complaint to the UI
    private fun displayComplaint(complaint: Complaint) {
        // Inflate the complaint card layout
        val complaintCardView = LayoutInflater.from(this).inflate(R.layout.complaint_item, complaintsContainer, false)

        // Bind the complaint data to the views in the card
        val titleTextView: TextView = complaintCardView.findViewById(R.id.complaintTitle)
        val detailsTextView: TextView = complaintCardView.findViewById(R.id.complaintDetails)
        val stateTextView: TextView = complaintCardView.findViewById(R.id.complaintState)
        val progressTextView: TextView = complaintCardView.findViewById(R.id.complaintProgress)

        titleTextView.text = complaint.title
        detailsTextView.text = complaint.details
        stateTextView.text = complaint.state
        progressTextView.text = complaint.progress

        // Add the complaint card to the container
        complaintsContainer.addView(complaintCardView)
    }
}
