package com.example.ailawyer

import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ailawyer.databinding.ActivityAddReportBinding

class AddReportActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddReportBinding
    private lateinit var complaintAdapter: ComplaintAdapter

    // 1. Data class for a Complaint
    data class Complaint(
        val title: String,
        val details: String,
        val state: String,
        val progress: String
    )  : Parcelable {
        constructor(parcel: Parcel) : this(
            parcel.readString().toString(),
            parcel.readString().toString(),
            parcel.readString().toString(),
            parcel.readString().toString()
        ) {
        }

        override fun writeToParcel(parcel: Parcel, flags: Int) {
            parcel.writeString(title)
            parcel.writeString(details)
            parcel.writeString(state)
            parcel.writeString(progress)
        }

        override fun describeContents(): Int {
            return 0
        }

        companion object CREATOR : Parcelable.Creator<Complaint> {
            override fun createFromParcel(parcel: Parcel): Complaint {
                return Complaint(parcel)
            }

            override fun newArray(size: Int): Array<Complaint?> {
                return arrayOfNulls(size)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Inflate using View Binding
        binding = ActivityAddReportBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 2. Create some dummy complaint items
        val complaints = mutableListOf(
            Complaint("Payment Stuck", "23 Oct, 2024", "Regular", "Pending"),
            Complaint("AI not responding", "1 Nov, 2024", "Regular", "Pending")
        )

        // 3. Set up RecyclerView
        binding.rvComplaints.layoutManager = LinearLayoutManager(this)
        complaintAdapter = ComplaintAdapter(complaints)
        binding.rvComplaints.adapter = complaintAdapter

        // 4. Add New Report button
        binding.btnAddReport.setOnClickListener {
            // Example: Add a new complaint
            complaints.add(Complaint("New Issue", "12 Dec, 2024", "Urgent", "In Progress"))
            complaintAdapter.notifyItemInserted(complaints.size - 1)
        }

        // 5. Home button
        binding.btnHome.setOnClickListener {
            // Example: Navigate to home or finish this activity
            finish()
        }
    }

    // 6. The adapter as an INNER CLASS
    inner class ComplaintAdapter(private val complaintList: MutableList<Complaint>) :
        RecyclerView.Adapter<ComplaintAdapter.ComplaintViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ComplaintViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val view = inflater.inflate(R.layout.complaint_item, parent, false)
            return ComplaintViewHolder(view)
        }

        override fun onBindViewHolder(holder: ComplaintViewHolder, position: Int) {
            val complaint = complaintList[position]
            holder.bind(complaint)
        }

        override fun getItemCount(): Int = complaintList.size

        // 7. ViewHolder
        inner class ComplaintViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            private val complaintTitle: TextView = itemView.findViewById(R.id.complaintTitle)
            private val complaintDetails: TextView = itemView.findViewById(R.id.complaintDetails)
            private val complaintState: TextView = itemView.findViewById(R.id.complaintState)
            private val complaintProgress: TextView = itemView.findViewById(R.id.complaintProgress)
            private val closeButton: ImageView = itemView.findViewById(R.id.closeButton)

            fun bind(complaint: Complaint) {
                complaintTitle.text = complaint.title
                complaintDetails.text = complaint.details
                complaintState.text = "State: ${complaint.state}"
                complaintProgress.text = "Progress: ${complaint.progress}"

                // If user taps the close (X) button, remove this complaint
                closeButton.setOnClickListener {
                    val pos = adapterPosition
                    if (pos != RecyclerView.NO_POSITION) {
                        complaintList.removeAt(pos)
                        notifyItemRemoved(pos)
                    }
                }
            }
        }
    }
}
