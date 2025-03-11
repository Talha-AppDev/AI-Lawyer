package com.example.ailawyer

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.ailawyer.databinding.ActivityChatscreenBinding

class ChatscreenActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChatscreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityChatscreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.

    }
}