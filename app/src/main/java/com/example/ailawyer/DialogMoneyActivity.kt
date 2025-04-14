package com.example.ailawyer

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import com.example.ailawyer.databinding.ActivityDialogMoneyBinding

class DialogMoneyActivity() : AppCompatActivity() {
    private lateinit var binding: ActivityDialogMoneyBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDialogMoneyBinding.inflate(layoutInflater)
        enableEdgeToEdge()


        setContentView(binding.root)
        binding.ivCancel.setOnClickListener{
            startActivity(Intent(this,MyWalletActivity::class.java))
        }

        binding.btnConfirm.setOnClickListener{
            //   startActivity(Intent(this,Payment_completeness_Dialog::class.java))


// Create an Intent to pass the complaint data to the add_report activity


        }

    }
}