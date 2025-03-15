package com.example.ailawyer

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.ailawyer.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class loginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.register.setOnClickListener {
            startActivity(Intent(this@loginActivity, RegisterActivity::class.java))
        }

        binding.btnLogin.setOnClickListener {
            // Retrieve email and password when the button is clicked
            val email = binding.etEmail.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()

            // Optional: Validate inputs
            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please enter email and password", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            loginUser(this, email, password) { success, user, errorMessage ->
                if (success) {
                    Toast.makeText(this, "Welcome, ${user?.email}", Toast.LENGTH_LONG).show()
                    startActivity(Intent(this@loginActivity, CityActivity::class.java))
                } else {
                    Toast.makeText(this, "Error: $errorMessage", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    fun loginUser(
        context: Context,
        email: String,
        password: String,
        callback: (Boolean, FirebaseUser?, String?) -> Unit
    ) {
        val auth = FirebaseAuth.getInstance()

        // Show progress bar and disable login button
        binding.progressBar.visibility = View.VISIBLE
        binding.btnLogin.isEnabled = false

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                // Hide progress bar and enable login button
                binding.progressBar.visibility = View.GONE
                binding.btnLogin.isEnabled = true

                if (task.isSuccessful) {
                    val user = auth.currentUser
                    Toast.makeText(context, "Login successful!", Toast.LENGTH_SHORT).show()
                    callback(true, user, null)
                } else {
                    val errorMessage = task.exception?.message ?: "Login failed!"
                    Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
                    callback(false, null, errorMessage)
                }
            }
    }
}
