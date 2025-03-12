package com.example.ailawyer

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ailawyer.QueryData.QueryRequest
import com.example.ailawyer.Network.RetrofitClient
import com.example.ailawyer.databinding.ActivityChatBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

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
                // Add user's message
                val time = getTimeParts()
                val userMessage = Message(messageText, time, isUser = true)
                messages.add(userMessage)
                chatAdapter.notifyItemInserted(messages.size - 1)
                binding.messageEditText.text.clear()
                binding.chatRecyclerView.smoothScrollToPosition(messages.size - 1)

                // Make the network call in a background coroutine
                lifecycleScope.launch(Dispatchers.IO) {
                    val legalResponse = getLegalResponse(QueryRequest(messageText))
                    withContext(Dispatchers.Main) {
                        val responseTime = getTimeParts()
                        messages.add(Message(legalResponse, responseTime, isUser = false))
                        chatAdapter.notifyItemInserted(messages.size - 1)
                        binding.chatRecyclerView.smoothScrollToPosition(messages.size - 1)
                    }
                }
            } else {
                Toast.makeText(this, "Please enter a message", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun getLegalResponse(queryRequest: QueryRequest): String {
        return try {
            // Pass the full QueryRequest object rather than just queryRequest.query
            val response = RetrofitClient.instance.queryLawyer(queryRequest).execute()
            if (response.isSuccessful && response.body() != null) {
                response.body()!!.response
            } else {
                "Request failed with status code: ${response.code()}"
            }
        } catch (e: Exception) {
            e.printStackTrace()
            e.message ?: "An error occurred"
        }
    }

    // Returns the current time as a formatted string (hours, minutes, and AM/PM)
    fun getTimeParts(): String {
        val currentDate = Date()
        val hours = SimpleDateFormat("hh", Locale.getDefault()).format(currentDate)
        val minutes = SimpleDateFormat("mm", Locale.getDefault()).format(currentDate)
        val amPm = SimpleDateFormat("a", Locale.getDefault()).format(currentDate)
        return "$hours$minutes$amPm"
    }
}
