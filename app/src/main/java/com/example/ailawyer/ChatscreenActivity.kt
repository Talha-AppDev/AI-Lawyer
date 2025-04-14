package com.example.ailawyer

import android.app.AlertDialog
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ailawyer.adapters.ChatAdapter
import com.example.ailawyer.databinding.ActivityChatscreenBinding
import com.example.ailawyer.dataclasses.Message
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ChatscreenActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChatscreenBinding
    private lateinit var chatAdapter: ChatAdapter
    private val messages = mutableListOf<Message>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatscreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val lawyerName = intent.getStringExtra("LawyerName") ?: "Lawyer"
        binding.tvName.text = lawyerName

        // Initialize chat adapter and RecyclerView
        chatAdapter = ChatAdapter(messages)
        binding.recyclerViewChat.apply {
            adapter = chatAdapter
            layoutManager = LinearLayoutManager(this@ChatscreenActivity)
        }

        // Set click listener for back button
        binding.btnBack.setOnClickListener {
            startActivity(Intent(this, IntroActivity::class.java))
        }

        // Set click listener for contract button to show a dialog
        binding.btnContract.setOnClickListener {
            showContractNotAvailableDialog()
        }
    }

    private fun getTimeParts(): String {
        val currentDate = Date()
        val hours = SimpleDateFormat("hh", Locale.getDefault()).format(currentDate)
        val minutes = SimpleDateFormat("mm", Locale.getDefault()).format(currentDate)
        val amPm = SimpleDateFormat("a", Locale.getDefault()).format(currentDate)
        return "$hours:$minutes $amPm"
    }

    private fun showContractNotAvailableDialog() {
        AlertDialog.Builder(this)
            .setTitle("Information")
            .setMessage("Contract is not available")
            .setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss()
            }
            .create()
            .show()
    }
}
