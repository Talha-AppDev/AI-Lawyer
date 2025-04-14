package com.example.ailawyer

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.ailawyer.databinding.ActivityAddMoneyBinding

class AddMoneyActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddMoneyBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddMoneyBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)

        // Inflate the custom dialog layout
        val dialogView = LayoutInflater.from(this).inflate(R.layout.activity_dialog_money, null)
        val inputField = dialogView.findViewById<EditText>(R.id.etAmount)
        val cancelIcon = dialogView.findViewById<ImageView>(R.id.ivCancel)
        val confirmButton = dialogView.findViewById<Button>(R.id.btnConfirm)

        // Create and show the "Add Money" dialog
        val dialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .create()
        dialog.show()

        // Close the dialog when clicking the cancel icon
        cancelIcon.setOnClickListener {
            dialog.dismiss()
        }

        // Handle confirmation button click
        confirmButton.setOnClickListener {
            val inputAmount = inputField.text.toString()

            if (inputAmount.isBlank()) {
                Toast.makeText(this, "Please enter a valid amount", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Amount: $inputAmount", Toast.LENGTH_LONG).show()
                dialog.dismiss()

                // Show the "Payment Completeness" dialog
                val dialogView2 = LayoutInflater.from(this).inflate(R.layout.activity_payment_completeness_dialog, null)
                val dialog2 = AlertDialog.Builder(this)
                    .setView(dialogView2)
                    .create()
                dialog2.show()

                val confirmButton2 = dialogView2.findViewById<Button>(R.id.button2)
                confirmButton2.setOnClickListener {
                    dialog2.dismiss()

                    // Go back to MyWallet and pass the amount
                    val intent = Intent(this, MyWalletActivity::class.java)
                    intent.putExtra("Amount", inputAmount)
                    startActivity(intent)

                    // Finish this activity so it doesn’t return
                    finish()
                }
            }
        }
    }
}