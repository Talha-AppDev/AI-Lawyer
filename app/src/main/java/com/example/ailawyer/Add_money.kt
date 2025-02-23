package com.example.ailawyer

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.ailawyer.databinding.ActivityAddMoneyBinding
import com.example.ailawyer.databinding.ActivityDialogMoneyBinding

class Add_money : AppCompatActivity() {
    private lateinit var binding: ActivityAddMoneyBinding
    private lateinit var binding2: ActivityDialogMoneyBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddMoneyBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)

        // Inflate the custom dialog layout
        val dialogView = LayoutInflater.from(this).inflate(R.layout.activity_dialog_money, null)

        // Find the input field in the custom layout
        val inputField = dialogView.findViewById<EditText>(R.id.etAmount)
        val cancelIcon = dialogView.findViewById<ImageView>(R.id.ivCancel)
        val confirmButton = dialogView.findViewById<Button>(R.id.btnConfirm)

        // Create and show the dialog
        val dialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .create()
        dialog.show()

        // Dismiss the dialog when clicking on the cancel icon (cross)
        cancelIcon.setOnClickListener {
            dialog.dismiss()  // Close the dialog
        }

        // Dismiss the dialog and handle input when clicking the confirm button
        confirmButton.setOnClickListener {
            val inputAmount = inputField.text.toString()

            // Check if the amount is valid
            if (inputAmount.isBlank()) {
                Toast.makeText(this, "Please enter a valid amount", Toast.LENGTH_SHORT).show()
            } else {
                // Do something with the input amount, for example show it in a Toast
                Toast.makeText(this, "Amount: $inputAmount", Toast.LENGTH_LONG).show()
                dialog.dismiss()
                Handler(Looper.getMainLooper()).postDelayed({
                    // Create and show the dialog after the delay
                    val dialog = AlertDialog.Builder(this)
                        .setView(dialogView)
                        .setCancelable(false)
                        .create()
                    dialog.show()
                },15000)
            // Close the dialog after confirming
                val dialogView2 = LayoutInflater.from(this).inflate(R.layout.activity_payment_completeness_dialog, null)
                val dialog2 = AlertDialog.Builder(this)
                    .setView(dialogView2)
                    .create()
                dialog2.show()
                val confirmButton2 = dialogView2.findViewById<Button>(R.id.button2)
                confirmButton2.setOnClickListener {
                    dialog2.dismiss()
                    Handler(Looper.getMainLooper()).postDelayed({
                        // Create and show the dialog after the delay
                        val dialog = AlertDialog.Builder(this)
                            .setView(dialogView)
                            .setCancelable(false)
                            .create()
                        dialog.show()
                    }, 15000)
                    startActivity(Intent(this, MyWallet::class.java))
                }
            }
        }
    }
}

