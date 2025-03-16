package com.example.ailawyer

import android.graphics.Typeface
import android.os.Bundle
import android.text.Html
import android.text.Spannable
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.style.AbsoluteSizeSpan
import android.text.style.RelativeSizeSpan
import android.text.style.StyleSpan
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ailawyer.QueryData.QueryRequest
import com.example.ailawyer.QueryData.QueryResponse
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
        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayShowTitleEnabled(false)

        binding.chatRecyclerView.layoutManager = LinearLayoutManager(this)
        chatAdapter = ChatAdapter(messages)
        binding.chatRecyclerView.adapter = chatAdapter



        binding.sendButton.setOnClickListener {

            // Show progress bar before making the network call
            binding.progressBar.visibility = View.VISIBLE

            val messageText = binding.messageEditText.text.toString().trim()
            if (messageText.isNotEmpty()) {
                val time = getTimeParts()
                val userMessage = Message(messageText, time, isUser = true)
                messages.add(userMessage)
                chatAdapter.notifyItemInserted(messages.size - 1)
                binding.messageEditText.text.clear()
                binding.chatRecyclerView.smoothScrollToPosition(messages.size - 1)

                lifecycleScope.launch(Dispatchers.IO) {
                    val legalResponse = getLegalResponse(QueryRequest(messageText))
                    withContext(Dispatchers.Main) {
                        // Use HTML to bold the labels
                        val formattedResponse = buildFormattedResponse(legalResponse)
                        val responseTime = getTimeParts()
                        messages.add(Message(formattedResponse.toString(), responseTime, isUser = false))
                        // Hide progress bar once the response is received
                        binding.progressBar.visibility = View.GONE
                        chatAdapter.notifyItemInserted(messages.size - 1)
                        binding.chatRecyclerView.smoothScrollToPosition(messages.size - 1)
                    }
                }
            } else {
                Toast.makeText(this, "Please enter a message", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun getLegalResponse(queryRequest: QueryRequest): QueryResponse {
        return try {
            val response = RetrofitClient.instance.queryLawyer(queryRequest).execute()
            if (response.isSuccessful && response.body() != null) {
                response.body()!!
            } else {
                QueryResponse(
                    additional_info = "Request failed with status code: ${response.code()}",
                    detailed_legal_guidance = "No detailed guidance available",
                    response = "Request failed"
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
            QueryResponse(
                additional_info = e.message ?: "An error occurred",
                detailed_legal_guidance = "No detailed guidance available",
                response = "Error occurred"
            )
        }
    }

    fun buildFormattedResponse(legalResponse: QueryResponse): SpannableStringBuilder {
        val builder = SpannableStringBuilder()

        // Define labels with a line break after them
        val additionalLabel = "Additional Info:\n"
        val detailedLabel = "Detailed Guidance:\n"
        val responseLabel = "Response:\n"

        // Create a span that scales text to 1.5x the base size
        val sizeSpan = RelativeSizeSpan(1.5f)

        // Append Additional Info
        val additionalSpannable = SpannableString(additionalLabel)
        additionalSpannable.setSpan(StyleSpan(Typeface.BOLD), 0, additionalLabel.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        additionalSpannable.setSpan(sizeSpan, 0, additionalLabel.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        builder.append(additionalSpannable)
        builder.append(legalResponse.additional_info)
        builder.append("\n\n")

        // Append Detailed Guidance
        val detailedSpannable = SpannableString(detailedLabel)
        detailedSpannable.setSpan(StyleSpan(Typeface.BOLD), 0, detailedLabel.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        detailedSpannable.setSpan(sizeSpan, 0, detailedLabel.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        builder.append(detailedSpannable)
        builder.append(legalResponse.detailed_legal_guidance)
        builder.append("\n\n")

        // Append Response
        val responseSpannable = SpannableString(responseLabel)
        responseSpannable.setSpan(StyleSpan(Typeface.BOLD), 0, responseLabel.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        responseSpannable.setSpan(sizeSpan, 0, responseLabel.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        builder.append(responseSpannable)
        builder.append(legalResponse.response)

        return builder
    }

    fun getTimeParts(): String {
        val currentDate = Date()
        val hours = SimpleDateFormat("hh", Locale.getDefault()).format(currentDate)
        val minutes = SimpleDateFormat("mm", Locale.getDefault()).format(currentDate)
        val amPm = SimpleDateFormat("a", Locale.getDefault()).format(currentDate)
        return "$hours:$minutes $amPm"
    }
}
