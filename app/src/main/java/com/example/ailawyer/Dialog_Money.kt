package com.example.ailawyer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import com.example.ailawyer.databinding.ActivityDialogMoneyBinding

 class Dialog_Money() : AppCompatActivity() {
    private lateinit var binding: ActivityDialogMoneyBinding



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDialogMoneyBinding.inflate(layoutInflater)
        enableEdgeToEdge()


        setContentView(binding.root)
    }
}