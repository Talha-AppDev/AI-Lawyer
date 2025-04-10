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
        binding.Wallet.setOnClickListener {
            startActivity(Intent(this, MyWallet::class.java))
        }

        binding.EditProfile.setOnClickListener {
            startActivity(Intent(this, EditprofileActivity::class.java))
        }

        binding.backButton.setOnClickListener {
            startActivity(Intent(this, CityResultActivity::class.java))
        }

        binding.report.setOnClickListener {
            startActivity(Intent(this, add_report::class.java))
        }


        setContentView(binding.root)
    }
}