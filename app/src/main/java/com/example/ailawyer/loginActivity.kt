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
        Toast.makeText(this, "User Type: $userType", Toast.LENGTH_SHORT).show()


        binding.register.setOnClickListener {
            val intent = Intent(this@loginActivity, RegisterActivity::class.java)
            intent.putExtra("userType", userType?: "")
            startActivity(intent)
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

            if (userType != null) {
                loginUser(this, email, password, userType) { success, user, errorMessage, userTypeResult ->
                    if (success) {
                        startActivity(Intent(this, CityActivity::class.java))
                    } else {
                        Toast.makeText(this, "Error: $errorMessage", Toast.LENGTH_LONG).show()
                    }
                }
            }

        }
    }

    fun loginUser(
        context: Context,
        email: String,
        password: String,
        userType: String, // Specify expected user type: "lawyer" or "client"
        callback: (Boolean, FirebaseUser?, String?, String?) -> Unit // Last parameter returns userType on success
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

                    // Ensure that the user is not null
                    if (user == null) {
                        callback(false, null, "User authentication succeeded but user is null", null)
                        return@addOnCompleteListener
                    }

                    // Determine the collection based on the provided userType
                    val collectionName = when (userType.lowercase(Locale.getDefault())) {
                        "lawyer" -> "lawyers"
                        "client" -> "clients"
                        else -> {
                            callback(false, user, "Invalid userType specified.", null)
                            return@addOnCompleteListener
                        }
                    }

                    // Query Firestore collection for extra user details
                    val firestore = FirebaseFirestore.getInstance()
                    firestore.collection(collectionName)
                        .document(user.uid)
                        .get()
                        .addOnSuccessListener { document ->
                            if (document.exists()) {
                                // Found the user document in the expected collection
                                callback(true, user, null, userType)
                            } else {
                                // Document not found in the specified collection
                                callback(false, user, "You are not availabe in $collectionName collection.", null)
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
