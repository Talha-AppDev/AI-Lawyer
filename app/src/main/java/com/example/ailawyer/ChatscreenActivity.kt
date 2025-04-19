package com.example.ailawyer

import android.app.AlertDialog
import android.content.Context
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
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.Query
import java.text.SimpleDateFormat
import java.util.*

class ChatscreenActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChatscreenBinding
    private lateinit var chatAdapter: ChatAdapter
    private val messages = mutableListOf<Message>()

    private val TAG = "ChatscreenActivity"
    private val DATE_FORMAT = SimpleDateFormat("hh:mm a", Locale.getDefault())

    private lateinit var clientId: String
    private lateinit var clientName: String
    private lateinit var lawyerId: String
    private lateinit var lawyerName: String
    private lateinit var conversationId: String
    private lateinit var currentUserId: String
    private lateinit var userType: String
    private lateinit var imgId: String
    private var messageListenerRegistration: ListenerRegistration? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatscreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupUserInfo()
        setupChat()
        setupUI()
    }

    override fun onStart() {
        super.onStart()
        messageListenerRegistration = listenForMessages()
    }

    override fun onStop() {
        super.onStop()
        messageListenerRegistration?.remove()
        messageListenerRegistration = null
    }



    private fun setupUserInfo() {
        currentUserId = FirebaseAuth.getInstance().currentUser?.uid ?: run {
            finish()
            return
        }

        val prefs = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        userType = prefs.getString("userType", "") ?: ""

        if (userType == "Client")
        {
            lawyerId = intent.getStringExtra("LawyerId").toString()
            lawyerName = intent.getStringExtra("LawyerName").toString()
            binding.tvName.text = lawyerName
            imgId = intent.getStringExtra("LawyerImageKey").toString()
            clientId = currentUserId
        }
        else
        {
            clientId = intent.getStringExtra("ClientId").toString()
            clientName = intent.getStringExtra("ClientName").toString()
            binding.tvName.text = clientName
            binding.tvSubtitle.text = "Client"
            imgId = intent.getStringExtra("ClientImageKey").toString()
            lawyerId = currentUserId
        }

        Log.w(TAG,"Type: $userType + ClientId: $clientId + LawyerId: $lawyerId")

        conversationId = makeConversationId(clientId, lawyerId)
        val resId = resources.getIdentifier(imgId, "drawable", packageName)
        if (resId != 0) {
            binding.profileImage.setImageResource(resId)
        } else {
            Log.w(TAG, "Profile image not found for key: $imgId")
            binding.profileImage.setImageResource(R.drawable.a)
        }
    }

    private fun setupChat() {
        FirebaseFirestore.getInstance().collection("chats").document(conversationId)
            .get()
            .addOnSuccessListener { document ->
                if (!document.exists()) {
                    initializeChatDocument()
                }
            }
    }

    private fun initializeChatDocument() {
        val chatData = hashMapOf(
            "createdAt" to FieldValue.serverTimestamp(),
            "participants" to listOf(clientId, lawyerId)
        )
        FirebaseFirestore.getInstance().collection("chats").document(conversationId)
            .set(chatData)
            .addOnFailureListener { e ->
                Log.e(TAG, "Error initializing chat document", e)
            }
    }

    private fun setupUI() {
        chatAdapter = ChatAdapter(messages)
        binding.recyclerViewChat.apply { adapter = chatAdapter
            layoutManager = LinearLayoutManager(this@ChatscreenActivity).apply {
                stackFromEnd = false  // Automatically scroll to bottom when new items are added
            }
            setHasFixedSize(true) // Optimization if item height doesn't change
        }

        binding.btnBack.setOnClickListener { finish() }
        binding.btnContract.setOnClickListener { showContractNotAvailableDialog() }
        binding.sendBTN.setOnClickListener { sendMessage() }
    }

    private fun sendMessage() {
        val text = binding.etMessage.text.toString().trim()
        if (text.isEmpty()) return

        binding.etMessage.text?.clear()

        val receiverId = if (userType == "Client") lawyerId else clientId

        // 👇 1. Optimistically update UI
        val time = DATE_FORMAT.format(Date()) // Local current time
        val tempMessage = Message(text, time, true)
        messages.add(tempMessage)
        chatAdapter.notifyItemInserted(messages.size - 1)
        scrollToBottom()

        // 👇 2. Disable send button temporarily
        binding.sendBTN.isEnabled = false

        // 👇 3. Send to Firestore in the background
        val messageData = hashMapOf(
            "text" to text,
            "senderId" to currentUserId,
            "receiverId" to receiverId,
            "timestamp" to FieldValue.serverTimestamp()
        )

        FirebaseFirestore.getInstance().collection("chats").document(conversationId)
            .collection("messages").add(messageData)
            .addOnSuccessListener {
                binding.sendBTN.isEnabled = true
            }
            .addOnFailureListener { e ->
                Log.e(TAG, "Error sending message", e)
                binding.sendBTN.isEnabled = true
                Toast.makeText(this, "Message send failed", Toast.LENGTH_SHORT).show()
            }
    }

    private fun listenForMessages(): ListenerRegistration {
        return FirebaseFirestore.getInstance().collection("chats").document(conversationId)
            .collection("messages")
            .orderBy("timestamp", Query.Direction.ASCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    Log.e(TAG, "Listen error", error)
                    return@addSnapshotListener
                }

                snapshot?.let {
                    messages.clear()
                    for (document in it.documents) {
                        val text = document.getString("text") ?: ""
                        val senderId = document.getString("senderId") ?: ""
                        val timestamp = document.getTimestamp("timestamp")?.toDate()

                        val time = timestamp?.let { DATE_FORMAT.format(it) } ?: "Sending..."
                        val isUser = senderId == currentUserId
                        messages.add(Message(text, time, isUser))
                    }
                    chatAdapter.notifyDataSetChanged()
                    scrollToBottom()
                }
            }
    }

    private fun scrollToBottom() {
        if (messages.isNotEmpty()) {
            binding.recyclerViewChat.post {
                binding.recyclerViewChat.smoothScrollToPosition(messages.size - 1)
            }
        }
    }

    private fun showContractNotAvailableDialog() {
        AlertDialog.Builder(this)
            .setTitle("Information")
            .setMessage("Contract feature is not available yet")
            .setPositiveButton("OK", null)
            .show()
    }

    private fun makeConversationId(id1: String, id2: String): String {
        return if (id1 < id2) {
            "${id1}_$id2"
        } else {
            "${id2}_$id1"
        }
    }
}