package com.example.ailawyer

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ailawyer.databinding.ActivityChatBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

// Assuming you have a Message data class like this:
//data class Message(val text: String, val time: String, val isUser: Boolean)

class AiChatActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChatBinding
    private lateinit var chatAdapter: ChatAdapter
    private val messages = mutableListOf<Message>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Initialize view binding
        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Setup toolbar (assuming toolbar is defined in your activity_chat.xml)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        // Setup RecyclerView
        binding.chatRecyclerView.layoutManager = LinearLayoutManager(this)
        chatAdapter = ChatAdapter(messages)
        binding.chatRecyclerView.adapter = chatAdapter

        // Send button click listener
        binding.sendButton.setOnClickListener {
            val messageText = binding.messageEditText.text.toString().trim()
            if (messageText.isNotEmpty()) {
                // Get current time in parts and format it
                val timeParts = getTimeParts()
                val formattedTime = "${timeParts.first}:${timeParts.second} ${timeParts.third}"

                // Create a new message and add it to the list
                val message = Message(messageText, formattedTime, isUser = true)
                messages.add(message)
                chatAdapter.notifyItemInserted(messages.size - 1)

                // Clear the message input field
                binding.messageEditText.text.clear()

                // Scroll to the new message
                binding.chatRecyclerView.smoothScrollToPosition(messages.size - 1)
            } else {
                Toast.makeText(this, "Please enter a message", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Function to return the current time as Triple (hours, minutes, AM/PM)
    fun getTimeParts(): Triple<String, String, String> {
        val currentDate = Date()
        val hours = SimpleDateFormat("hh", Locale.getDefault()).format(currentDate)
        val minutes = SimpleDateFormat("mm", Locale.getDefault()).format(currentDate)
        val amPm = SimpleDateFormat("a", Locale.getDefault()).format(currentDate)
        return Triple(hours, minutes, amPm)
    }
}
