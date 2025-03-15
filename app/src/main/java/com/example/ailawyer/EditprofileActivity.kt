package com.example.ailawyer

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.ailawyer.databinding.ActivityEditprofileBinding
import com.example.ailawyer.databinding.ActivitySettingsBinding

class EditprofileActivity : AppCompatActivity() {

    lateinit var binding: ActivityEditprofileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditprofileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnBack.setOnClickListener {
            startActivity(Intent(this, Settings::class.java))
        }
        binding.fabHome.setOnClickListener{
            startActivity(Intent(this, CityResultActivity::class.java))
        }
    }
}