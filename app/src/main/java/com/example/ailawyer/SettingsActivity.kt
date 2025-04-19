package com.example.ailawyer

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.ailawyer.databinding.ActivitySettingsBinding
import com.example.ailawyer.dataclasses.add_report

class SettingsActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySettingsBinding
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)



        val sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE)
        val type = sharedPreferences.getString("userType", "")

        binding.Wallet.setOnClickListener {
            if (type == "Lawyer") {
                startActivity(Intent(this, MyWalletActivity::class.java))
            } else {

                Toast.makeText(this, "Only lawyers can access the wallet.", Toast.LENGTH_SHORT).show()
            }
        }

        binding.report.setOnClickListener {
            if (type == "Client") {
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
