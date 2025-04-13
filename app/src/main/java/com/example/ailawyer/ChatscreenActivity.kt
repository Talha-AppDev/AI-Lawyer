package com.example.ailawyer

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ailawyer.databinding.ActivityChatscreenBinding
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
        binding.btnChatbot.setOnClickListener {
            val intent = Intent(this, AiChatActivity::class.java)

            startActivity(intent)
        }

        // Set click listener for sending a message
        binding.imgEndIcon.setOnClickListener {
            val messageText = binding.etMessage.text.toString().trim()
            if (messageText.isNotEmpty()) {
                val time = getTimeParts()
                // Add user's message to the chat list
                val userMessage = Message(messageText, time, isUser = true)
                messages.add(userMessage)
                chatAdapter.notifyItemInserted(messages.size - 1)
                binding.recyclerViewChat.smoothScrollToPosition(messages.size - 1)

                // Optionally clear the text field after sending
                binding.etMessage.text.clear()

                // Add system response to the chat list
                val responseMessage = Message("Asma J. is not available.", time, isUser = false)
                messages.add(responseMessage)
                chatAdapter.notifyItemInserted(messages.size - 1)
                binding.recyclerViewChat.smoothScrollToPosition(messages.size - 1)
            } else {
                Toast.makeText(this, "Please enter a message", Toast.LENGTH_SHORT).show()
            }
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
