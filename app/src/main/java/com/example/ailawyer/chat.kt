package com.example.ailawyer

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ChatAdapter(private val messages: List<Message>) :
    RecyclerView.Adapter<ChatAdapter.MessageViewHolder>() {

    class MessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val messageText: TextView = itemView.findViewById(R.id.messageText)
        val messageTime: TextView = itemView.findViewById(R.id.messageTime)
        //val messageContainer: LinearLayout = itemView.findViewById(R.id.messageContainer) // Container for alignment
    }

    @SuppressLint("ResourceType")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val layoutId = if (viewType == VIEW_TYPE_SENT) {
            R.layout.item_message_sent
        // Use separate layouts
        } else {
            R.layout.item_message_received

        }
        val view = LayoutInflater.from(parent.context).inflate(layoutId, parent, false)
        return MessageViewHolder(view)
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        val message = messages[position]
        holder.messageText.text = message.text
        holder.messageTime.text = message.time

        // No need to set layout direction here
    }

    override fun getItemCount(): Int = messages.size

    override fun getItemViewType(position: Int): Int {
        return if (messages[position].isUser) VIEW_TYPE_SENT else VIEW_TYPE_RECEIVED
    }

    companion object {
        private const val VIEW_TYPE_SENT = 1
        private const val VIEW_TYPE_RECEIVED = 2
    }
}



class chat : AppCompatActivity() {

    private lateinit var toolbar: Toolbar
    private lateinit var avatar: ImageView
    private lateinit var toolbarTitle: TextView
    private lateinit var toolbarSubtitle: TextView
    private lateinit var backButton: ImageView
    private lateinit var chatRecyclerView: RecyclerView
    private lateinit var chatAdapter: ChatAdapter
    private lateinit var attachButton: ImageView
    private lateinit var messageEditText: EditText
    private lateinit var sendButton: ImageView
    private var messages = mutableListOf<Message>()

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        toolbar = findViewById(R.id.toolbar)
        avatar = findViewById(R.id.avatar)

        chatRecyclerView = findViewById(R.id.chatRecyclerView)
        attachButton = findViewById(R.id.attachButton)
        messageEditText = findViewById(R.id.messageEditText)
        sendButton = findViewById(R.id.sendButton)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        chatRecyclerView.layoutManager = LinearLayoutManager(this)

        messages.add(Message("I need help with something", "9:28 AM", true))
        messages.add(Message("I need not help with something", "9:28 AM", false))
        messages.add(Message("Sure tell me what is it", "9:28 AM", false))
        messages.add(Message("I am sharing a file with you read it and analyze it", "9:28 AM", true))
        messages.add(Message("Send me the file I will analyze the file", "9:28 AM", false))
        messages.add(Message("Cyber bully case.pdf", "9:38 AM", true))
        messages.add(Message("This file is about the recent Cyber Bully case help in Quaid-e-Azam University Islamabad that ended in the suicide of 1 student", "9:28 AM", true))

        chatAdapter = ChatAdapter(messages)
        chatRecyclerView.adapter = chatAdapter
    }
}