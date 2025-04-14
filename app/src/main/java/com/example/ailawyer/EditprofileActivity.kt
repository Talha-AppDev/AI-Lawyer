package com.example.ailawyer

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.ailawyer.databinding.ActivityEditprofileBinding

class EditprofileActivity : AppCompatActivity() {

    lateinit var binding: ActivityEditprofileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditprofileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnBack.setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
        }
        binding.fabHome.setOnClickListener{
            startActivity(Intent(this, CityResultActivity::class.java))
        }
    }
}