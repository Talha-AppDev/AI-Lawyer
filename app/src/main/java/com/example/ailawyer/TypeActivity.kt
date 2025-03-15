package com.example.ailawyer

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.ailawyer.databinding.ActivityTypeBinding

class TypeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTypeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityTypeBinding.inflate(layoutInflater)
        setContentView(binding.root) // Move this before setting click listeners

        binding.lawyer.setOnClickListener {
            startActivity(Intent(this@TypeActivity, loginActivity::class.java))
        }
        binding.client.setOnClickListener {
            startActivity(Intent(this@TypeActivity, loginActivity::class.java))
        }
    }

}