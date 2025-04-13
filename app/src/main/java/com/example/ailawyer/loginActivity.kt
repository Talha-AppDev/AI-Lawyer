package com.example.ailawyer

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.ailawyer.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import java.util.Locale

class loginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val userType = intent.getStringExtra("userType")

        binding.register.setOnClickListener {
            val intent = Intent(this@loginActivity, RegisterActivity::class.java)
            intent.putExtra("userType", userType ?: "")
            startActivity(intent)
        }

        binding.btnLogin.setOnClickListener {
            val email = binding.etEmail.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please enter email and password", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (userType != null) {
                loginUser(
                    this,
                    email,
                    password,
                    userType
                ) { success, user, errorMessage, userTypeResult ->
                    if (success) {
                        val sharedPref = getSharedPreferences("UserPrefs", MODE_PRIVATE)
                        with(sharedPref.edit()) {
                            putString("userType", userType)
                            putString("userEmail", email)
                            putString("userPassword", password)
                            apply()
                        }

                        Log.d(
                            "SharedPrefDebug",
                            "✅ Saved in SharedPreferences → userType: $userType, email: $email, password: $password"
                        )

                        Toast.makeText(this, "Login as $userType", Toast.LENGTH_LONG).show()
                        startActivity(Intent(this, CityActivity::class.java))
                        finish()
                    }

                }
            }
        }

    }
    fun loginUser(
        context: Context,
        email: String,
        password: String,
        userType: String,
        callback: (Boolean, FirebaseUser?, String?, String?) -> Unit
    ) {
        val auth = FirebaseAuth.getInstance()

        binding.progressBar.visibility = View.VISIBLE
        binding.btnLogin.isEnabled = false

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                binding.progressBar.visibility = View.GONE
                binding.btnLogin.isEnabled = true

                if (task.isSuccessful) {
                    val user = auth.currentUser
                    if (user == null) {
                        callback(false, null, "User authentication succeeded but user is null", null)
                        return@addOnCompleteListener
                    }

                    val collectionName = when (userType.lowercase(Locale.getDefault())) {
                        "lawyer" -> "lawyers"
                        "client" -> "clients"
                        else -> {
                            callback(false, user, "Invalid userType specified.", null)
                            return@addOnCompleteListener
                        }
                    }

                    val firestore = FirebaseFirestore.getInstance()
                    firestore.collection(collectionName)
                        .document(user.uid)
                        .get()
                        .addOnSuccessListener { document ->
                            if (document.exists()) {
                                // ✅ Auth + Firestore match: now save in SharedPreferences
                                callback(true, user, null, userType)
                            } else {
                                callback(false, user, "You are not available in $collectionName collection.", null)
                            }
                        }
                        .addOnFailureListener { e ->
                            callback(false, user, "Error fetching user data: ${e.message}", null)
                        }
                } else {
                    val errorMessage = task.exception?.message ?: "Login failed!"
                    Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
                    callback(false, null, errorMessage, null)
                }
            }
    }
}
