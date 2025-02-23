package com.example.ailawyer

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class ADD_COMPLAINTS : AppCompatActivity() {

    private lateinit var uploadButton: TextView
    private lateinit var selectedFileName: TextView

    private var selectedFileUri: Uri? = null
    private val PICK_FILE_REQUEST = 1

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_complaints) // Replace with your layout file name

        uploadButton = findViewById(R.id.add_file)


        uploadButton.setOnClickListener {
            openFilePicker()
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