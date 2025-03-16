package com.example.ailawyer

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import com.example.ailawyer.databinding.ActivitySettingsBinding

class Settings : AppCompatActivity() {
    private lateinit var binding: ActivitySettingsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        binding.imageView5.setOnClickListener {
            startActivity(Intent(this, MyWallet::class.java))
        }
        setContentView(binding.root)
        binding.cardEdtProf.setOnClickListener {
            startActivity(Intent(this, EditprofileActivity::class.java))
        }
        binding.cardView2.setOnClickListener {
            startActivity(Intent(this, MyWallet::class.java))
        }
        binding.cardView5.setOnClickListener {
            startActivity(Intent(this, add_report::class.java))
        }
    }
}