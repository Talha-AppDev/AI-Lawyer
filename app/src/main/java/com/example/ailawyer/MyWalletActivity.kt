package com.example.ailawyer

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.ailawyer.databinding.ActivityMyWalletBinding

class MyWalletActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMyWalletBinding
    private lateinit var sharedPreferences: SharedPreferences
    var totalBalance: Float = 0.0f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyWalletBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPreferences = getSharedPreferences("wallet_prefs", MODE_PRIVATE)

        // Retrieve previous balance
        totalBalance = sharedPreferences.getFloat("total_balance", 0.0f)

        // Retrieve the new amount passed from Add_money
        val amount = intent.getStringExtra("Amount")?.toFloatOrNull() ?: 0.0f

        // Update total balance
        totalBalance += amount

        // Save updated balance to SharedPreferences
        sharedPreferences.edit().putFloat("total_balance", totalBalance).apply()

        // Display updated balance
        binding.balance.text = "$ $totalBalance"

        binding.addMoneyBtn.setOnClickListener {
            startActivity(Intent(this, AddMoneyActivity::class.java))
        }
    }
}