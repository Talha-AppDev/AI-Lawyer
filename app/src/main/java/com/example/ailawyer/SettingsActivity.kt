package com.example.ailawyer

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.ailawyer.databinding.ActivitySettingsBinding
import com.example.ailawyer.dataclasses.add_report

class SettingsActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySettingsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        binding.Wallet.setOnClickListener {
            startActivity(Intent(this, MyWalletActivity::class.java))
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