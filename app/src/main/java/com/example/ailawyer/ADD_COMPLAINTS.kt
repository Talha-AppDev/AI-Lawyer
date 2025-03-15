package com.example.ailawyer

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.ailawyer.databinding.ActivityAddComplaintsBinding
import com.example.ailawyer.databinding.ActivityAddReportBinding

class ADD_COMPLAINTS : AppCompatActivity() {
    private lateinit var binding :ActivityAddComplaintsBinding
    // val complaint =Complaint(" ","","","")

    private lateinit var uploadButton: TextView
    private lateinit var selectedFileName: TextView

    private var selectedFileUri: Uri? = null
    private val PICK_FILE_REQUEST = 1

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddComplaintsBinding.inflate(layoutInflater)
        setContentView(binding.root) // Replace with your layout file name

        uploadButton = binding.addFile


        uploadButton.setOnClickListener {
            openFilePicker()
        }
        binding.submitButton.setOnClickListener {
            val complaint = AddReportActivity.Complaint(
                title = binding.complaintTitle.text.toString(),
                details = binding.complaintDescription.text.toString(),
                state = binding.regularStatus.text.toString(),
                progress = "Pending" // or another default value
            )

// Create an Intent to pass the complaint data to the add_report activity
            val intent = Intent(this, AddReportActivity::class.java)
            intent.putExtra("complaintData", complaint) // Passing the complaint as a Parcelable extra
            startActivity(intent)
        }


    }

    private fun openFilePicker() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            type = "*/*" // Or specify specific MIME types if needed (e.g., "application/pdf")
            addCategory(Intent.CATEGORY_OPENABLE)
        }
        startActivityForResult(intent, PICK_FILE_REQUEST)
    }
}