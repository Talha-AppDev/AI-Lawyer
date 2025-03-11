package com.example.ailawyer

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

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