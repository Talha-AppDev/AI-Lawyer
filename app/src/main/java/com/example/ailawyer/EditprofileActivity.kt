package com.example.ailawyer

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.ailawyer.databinding.ActivityEditprofileBinding
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*

class EditprofileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditprofileBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditprofileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        // Load user data from SharedPreferences
        val sharedPref = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        val savedName = sharedPref.getString("userName", "")
        val savedEmail = sharedPref.getString("userEmail", "")
        val savedUserType = sharedPref.getString("userType", "")
        val savedPassword = sharedPref.getString("userPassword", "")

        binding.etName.setText(savedName)
        binding.etEmail.setText(savedEmail)

        binding.btnUpdateInfo.setOnClickListener {
            val newName = binding.etName.text.toString().trim()
            val newEmail = binding.etEmail.text.toString().trim()
            val currentPassword = binding.etOldPassword.text.toString().trim()

            Log.d("DEBUG", "Entered Password: $currentPassword")

            if (newName.isEmpty() || newEmail.isEmpty() || currentPassword.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            } else {
                updateUserProfile(newName, newEmail, currentPassword, savedUserType ?: "user")
            }
        }

        binding.btnBack.setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
        }

        binding.fabHome.setOnClickListener {
            startActivity(Intent(this, CityResultActivity::class.java))
        }
    }

    private fun updateUserProfile(newName: String, newEmail: String, currentPassword: String, userType: String) {
        val user = auth.currentUser
        if (user != null && user.email != null) {
            val credential = EmailAuthProvider.getCredential(user.email!!, currentPassword)
            Log.d("DEBUG", "UID: ${user.uid}")
            Log.d("DEBUG", "Current Email: ${user.email}")

            // Re-authenticate user with their current password
            user.reauthenticate(credential).addOnSuccessListener {
                Log.d("DEBUG", "Re-authentication success")

                // Check if the user's email is verified
                if (!user.isEmailVerified) {
                    Log.d("DEBUG", "Email not verified")
                    // Inform the user and send verification email
                    Toast.makeText(this, "Please verify your current email before updating it.", Toast.LENGTH_LONG).show()

                    // Send email verification again
                    user.sendEmailVerification().addOnSuccessListener {
                        Log.d("DEBUG", "Verification email sent!")
                        Toast.makeText(this, "Verification email sent! Please verify your email.", Toast.LENGTH_LONG).show()

                        // Optionally, guide the user to check their email and update the profile afterward.
                        // You can enable a "Retry" button here to retry the process after they have verified their email.
                    }.addOnFailureListener { ex ->
                        Log.e("DEBUG", "Failed to send verification email: ${ex.message}", ex)
                        Toast.makeText(this, "Failed to send verification email: ${ex.message}", Toast.LENGTH_LONG).show()
                    }
                    return@addOnSuccessListener // Don't proceed further until email is verified
                }

                // Proceed if email is verified
                Log.d("DEBUG", "Email verified. Proceeding with update.")

                // Update profile with the new name
                val profileUpdates = UserProfileChangeRequest.Builder()
                    .setDisplayName(newName)
                    .build()

                user.updateProfile(profileUpdates).addOnSuccessListener {
                    Log.d("DEBUG", "Display name updated")

                    // Update email address
                    user.updateEmail(newEmail).addOnSuccessListener {
                        Log.d("DEBUG", "Email updated")

                        // Force user to log out and log back in (important for email change)
                        auth.signOut()

                        // Proceed with Firestore update
                        val collectionName = when (userType.lowercase(Locale.getDefault())) {
                            "lawyer" -> "lawyers"
                            "client" -> "clients"
                            else -> "users"
                        }

                        val userData = mapOf(
                            "displayName" to newName,
                            "email" to newEmail
                        )

                        firestore.collection(collectionName).document(user.uid)
                            .update(userData)
                            .addOnSuccessListener {
                                // Update SharedPreferences
                                val sharedPref = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
                                with(sharedPref.edit()) {
                                    putString("userName", newName)
                                    putString("userEmail", newEmail)
                                    apply()
                                }

                                Toast.makeText(this, "Profile updated successfully!", Toast.LENGTH_SHORT).show()

                                // Optional: go back to settings
                                startActivity(Intent(this, SettingsActivity::class.java))
                                finish()
                            }
                            .addOnFailureListener { ex ->
                                Log.e("DEBUG", "Firestore update failed: ${ex.message}", ex)
                                Toast.makeText(this, "Firestore update failed: ${ex.message}", Toast.LENGTH_SHORT).show()
                            }

                    }.addOnFailureListener {
                        Log.e("DEBUG", "Email update failed", it)
                        Toast.makeText(this, "Email update failed: ${it.message}", Toast.LENGTH_LONG).show()
                    }

                }.addOnFailureListener {
                    Toast.makeText(this, "Display name update failed: ${it.message}", Toast.LENGTH_SHORT).show()
                }

                // Add the code for changing password here if you need to update the password as well
                val newPassword = binding.etNewPassword.text.toString().trim()  // Assuming you have an input field for new password

                if (newPassword.isNotEmpty()) {
                    user.updatePassword(newPassword).addOnSuccessListener {
                        Log.d("DEBUG", "Password updated successfully")
                        Toast.makeText(this, "Password updated successfully", Toast.LENGTH_SHORT).show()
                    }.addOnFailureListener {
                        Log.e("DEBUG", "Password update failed", it)
                        Toast.makeText(this, "Password update failed: ${it.message}", Toast.LENGTH_LONG).show()
                    }
                }

            }.addOnFailureListener {
                Log.e("DEBUG", "Re-authentication failed", it)
                Toast.makeText(this, "Re-authentication failed: ${it.message}", Toast.LENGTH_LONG).show()
            }

        } else {
            Toast.makeText(this, "No user found or user not logged in", Toast.LENGTH_SHORT).show()
        }
    }

}
