package com.example.ailawyer

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.ailawyer.databinding.ActivitySettingsBinding

class Settings : AppCompatActivity() {
    private lateinit var binding: ActivitySettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val type = intent.getStringExtra("usertype")?.lowercase()

        binding.Wallet.setOnClickListener {
            if (type == "lawyer") {
                startActivity(Intent(this, MyWallet::class.java))
            } else {
                Toast.makeText(this, "User Type: $type", Toast.LENGTH_SHORT).show()
                Toast.makeText(this, "Only lawyers can access the wallet.", Toast.LENGTH_SHORT).show()
            }
        }

        binding.report.setOnClickListener {
            if (type == "client") {
                startActivity(Intent(this, add_report::class.java))
            } else {
                Toast.makeText(this, "Only clients can add complaints.", Toast.LENGTH_SHORT).show()
            }
        }

        binding.EditProfile.setOnClickListener {
            startActivity(Intent(this, EditprofileActivity::class.java))
        }

        binding.backButton.setOnClickListener {
            startActivity(Intent(this, CityResultActivity::class.java))
        }
    }
}
