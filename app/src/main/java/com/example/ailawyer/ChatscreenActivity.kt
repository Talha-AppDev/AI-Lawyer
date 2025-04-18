package com.example.ailawyer

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ailawyer.adapters.ChatAdapter
import com.example.ailawyer.databinding.ActivityChatscreenBinding
import com.example.ailawyer.dataclasses.Message
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ChatscreenActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChatscreenBinding
    private lateinit var chatAdapter: ChatAdapter
    private val messages = mutableListOf<Message>()

    private val TAG = "ChatscreenActivity"

    private lateinit var clientId: String
    private lateinit var lawyerId: String
    private lateinit var conversationId: String
    private lateinit var currentUserId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatscreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Grab IDs and names/images for both roles from intent
        clientId = intent.getStringExtra("ClientId") ?: ""
        lawyerId = intent.getStringExtra("LawyerId") ?: ""
        val clientName = intent.getStringExtra("ClientName") ?: "Client"
        val lawyerName = intent.getStringExtra("LawyerName") ?: "Lawyer"
        val clientImageKey = intent.getStringExtra("ClientImageKey") ?: ""
        val lawyerImageKey = intent.getStringExtra("LawyerImageKey") ?: ""

        // Ensure IDs are provided
        if (clientId.isEmpty() || lawyerId.isEmpty()) {
            Log.e(TAG, "Missing clientId or lawyerId in intent extras. clientId=$clientId, lawyerId=$lawyerId")
            Toast.makeText(this, "Chat participants not defined.", Toast.LENGTH_LONG).show()
            finish()
            return
        }

        currentUserId = FirebaseAuth.getInstance().currentUser?.uid ?: ""
        Log.d(TAG, "Current user: $currentUserId (client:$clientId lawyer:$lawyerId)")

        // Determine chat partner
        val isCurrentClient = currentUserId == clientId
        val partnerId = if (isCurrentClient) lawyerId else clientId
        val partnerName = if (isCurrentClient) lawyerName else clientName
        val partnerImageKey = if (isCurrentClient) lawyerImageKey else clientImageKey

        // UI: show partner info
        binding.tvName.text = partnerName
        loadProfileImage(partnerImageKey)


        // Build a consistent conversation ID so both sides use same thread
        conversationId = listOf(clientId, lawyerId).sorted().joinToString("_")
        Log.d(TAG, "ConversationId: $conversationId")

        // Setup RecyclerView
        chatAdapter = ChatAdapter(messages)
        binding.recyclerViewChat.apply {
            adapter = chatAdapter
            layoutManager = LinearLayoutManager(this@ChatscreenActivity)
        }

        // Buttons
        binding.btnBack.setOnClickListener { finish() }
        binding.btnContract.setOnClickListener { showContractNotAvailableDialog() }
        binding.sendBTN.setOnClickListener { sendMessage(partnerId) }

        // Listen for messages
        listenForMessages()
    }

    private fun loadProfileImage(imageKey: String) {
        if (imageKey.isNotEmpty()) {
            val context = binding.profileImage.context
            val resId = context.resources.getIdentifier(imageKey, "drawable", context.packageName)
            if (resId != 0) binding.profileImage.setImageResource(resId)
            else Log.w(TAG, "Profile image key not found: $imageKey")
        }
    }

    private fun sendMessage(partnerId: String) {
        val messageText = binding.etMessage.text.toString().trim()
        if (messageText.isEmpty()) {
            Toast.makeText(this, "Please enter a message", Toast.LENGTH_SHORT).show()
            return
        }
        val time = getTimeParts()

        val messageMap = mapOf(
            "text" to messageText,
            "senderId" to currentUserId,
            "receiverId" to partnerId,
            "timestamp" to FieldValue.serverTimestamp()
        )
        Log.d(TAG, "Sending message: $messageMap")

        val db = FirebaseFirestore.getInstance()
        val chatRef = db.collection("chats").document(conversationId)
        chatRef.set(mapOf("createdAt" to FieldValue.serverTimestamp()), SetOptions.merge())
            .addOnSuccessListener {
                chatRef.collection("messages").add(messageMap)
                    .addOnSuccessListener {
                        messages.add(Message(messageText, time, isUser = (currentUserId == clientId)))
                        chatAdapter.notifyItemInserted(messages.size - 1)
                        binding.recyclerViewChat.smoothScrollToPosition(messages.size - 1)
                        binding.etMessage.text?.clear()
                    }
                    .addOnFailureListener { e ->
                        Toast.makeText(this, "Send failed: ${e.message}", Toast.LENGTH_SHORT).show()
                        Log.e(TAG, "Message add failed", e)
                    }
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Chat init failed: ${e.message}", Toast.LENGTH_SHORT).show()
                Log.e(TAG, "Chat doc init failed", e)
            }
    }

    private fun listenForMessages() {
        val db = FirebaseFirestore.getInstance()
        db.collection("chats").document(conversationId)
            .collection("messages").orderBy("timestamp")
            .addSnapshotListener { snap, err ->
                if (err != null) {
                    Log.e(TAG, "Listener error", err)
                    return@addSnapshotListener
                }
                if (snap != null && !snap.isEmpty) {
                    messages.clear()
                    for (doc in snap.documents) {
                        val text = doc.getString("text") ?: continue
                        val sender = doc.getString("senderId") ?: continue
                        val date = doc.getTimestamp("timestamp")?.toDate()
                        val tm = date?.let { formatTime(it) } ?: getTimeParts()
                        messages.add(Message(text, tm, isUser = (sender == currentUserId)))
                    }
                    chatAdapter.notifyDataSetChanged()
                    binding.recyclerViewChat.scrollToPosition(messages.size - 1)
                }
            }
    }

    private fun formatTime(date: Date): String =
        SimpleDateFormat("hh:mm a", Locale.getDefault()).format(date)

    private fun getTimeParts(): String {
        val now = Date()
        val h = SimpleDateFormat("hh", Locale.getDefault()).format(now)
        val m = SimpleDateFormat("mm", Locale.getDefault()).format(now)
        val a = SimpleDateFormat("a", Locale.getDefault()).format(now)
        return "$h:$m $a"
    }

    private fun showContractNotAvailableDialog() {
        AlertDialog.Builder(this)
            .setTitle("Information")
            .setMessage("Contract is not available")
            .setPositiveButton("OK") { dlg, _ -> dlg.dismiss() }
            .show()
    }
}
