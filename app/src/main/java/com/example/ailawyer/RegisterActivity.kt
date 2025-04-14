package com.example.ailawyer

import android.accounts.AccountManager
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.ailawyer.databinding.ActivityRegisterBinding
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.common.AccountPicker
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import java.util.Locale

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding

    private lateinit var user_name: EditText
    private lateinit var user_email: EditText
    private lateinit var user_pass: EditText
    private lateinit var user_confirm_pass: EditText
    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var signInLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityRegisterBinding.inflate(layoutInflater)

        user_name = binding.etName
        user_email = binding.etEmail
        user_pass = binding.etPassword
        user_confirm_pass = binding.etConfirmPassword

        val userType = intent.getStringExtra("userType")
        Toast.makeText(this@RegisterActivity, "User Type: $userType", Toast.LENGTH_SHORT).show()

        if (binding.progressBar.visibility == View.VISIBLE)
            binding.btnSignUp.isEnabled = false

        binding.btnSignUp.setOnClickListener {
            if (user_name.text.toString().isEmpty())
                Toast.makeText(this, "Name is empty", Toast.LENGTH_SHORT).show()
            else if(!isValidName(user_name.text.toString()))
                Toast.makeText(this, "Invalid name", Toast.LENGTH_SHORT).show()
            else if (user_email.text.toString().isEmpty())
                Toast.makeText(this, "Email is empty", Toast.LENGTH_SHORT).show()
            else if(!isEmailValid(user_email.text.toString()))
                Toast.makeText(this, "Invalid email", Toast.LENGTH_SHORT).show()
            else if (user_pass.text.toString().isEmpty())
                Toast.makeText(this, "Password is empty", Toast.LENGTH_SHORT).show()
            else if (user_pass.text.toString() != user_confirm_pass.text.toString())
                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
            else {
                if (isInternetAvailable(this)) {
                    signUpUser(
                        user_name.text.toString(),
                        user_email.text.toString(),
                        user_pass.text.toString(),
                        userType.toString()
                    )
                    { success ->
                        if (success) {
                            startActivity(Intent(this, CityActivity::class.java))
                        } else {
                            // Handle sign-up failure (e.g., log error or retry)
                        }
                    }

                }
                else
                    showNoInternetDialog()
            }
        }

        binding.btnGoogle.setOnClickListener {
            binding.progressBar.visibility = View.VISIBLE
            // Create an intent to pick a Google account
            val intent = AccountPicker.newChooseAccountIntent(
                null,                  // selectedAccount (null means no default)
                null,                  // allowableAccounts (null means all)
                arrayOf("com.google"), // Only show Google accounts
                false,                 // alwaysPromptForAccount (false means no forced prompt)
                null,                  // descriptionOverrideText
                null,                  // addAccountAuthTokenType
                null,                  // addAccountRequiredFeatures
                null                   // addAccountOptions
            )
            pickAccountLauncher.launch(intent)
        }

        binding.tvAlreadyAccount.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }
        setContentView(binding.root)
    }

    private fun setSignUpButtonEnabled(enabled: Boolean) {
        binding.btnSignUp.isEnabled = enabled
    }

    private fun signUpUser(
        userName: String,
        userEmail: String,
        userPass: String,
        userType: String,
        onSignUpResult: (Boolean) -> Unit
    ) {
        // Show the progress bar and disable the sign-up button
        binding.progressBar.visibility = View.VISIBLE
        setSignUpButtonEnabled(false)

        auth = FirebaseAuth.getInstance()

        auth.createUserWithEmailAndPassword(userEmail, userPass)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // New user created successfully
                    val user = auth.currentUser
                    val profileUpdates = UserProfileChangeRequest.Builder()
                        .setDisplayName(userName)
                        .build()

                    user?.updateProfile(profileUpdates)
                        ?.addOnCompleteListener { profileTask ->
                            if (profileTask.isSuccessful) {
                                // Determine the collection name based on the userType
                                val collectionName = when (userType.lowercase(Locale.getDefault())) {
                                    "lawyer" -> "lawyers"
                                    "client" -> "clients"
                                    else -> "users" // Fallback collection
                                }

                                // Prepare user data to store
                                val userData = hashMapOf(
                                    "userType" to userType,
                                    "email" to userEmail,
                                    "displayName" to userName
                                )

                                // Write extra user info to Firestore
                                val firestore = FirebaseFirestore.getInstance()
                                firestore.collection(collectionName)
                                    .document(user.uid)
                                    .set(userData)
                                    .addOnSuccessListener {
                                        // ✅ Save to SharedPreferences
                                        val sharedPref = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
                                        with(sharedPref.edit()) {
                                            putString("userType", userType)
                                            putString("userEmail", userEmail)
                                            putString("userPassword", userPass)
                                            putString("userName", userName)
                                            apply()
                                        }

                                        // ✅ Log the saved values
                                        Log.d(
                                            "SharedPrefDebug",
                                            "✅ SignUp -> Saved to SharedPreferences → Name: $userName, Email: $userEmail, Password: $userPass, Type: $userType"
                                        )

                                        Toast.makeText(
                                            this,
                                            "Sign up as $userType",
                                            Toast.LENGTH_SHORT
                                        ).show()

                                        // Hide the progress bar and re-enable the sign-up button
                                        binding.progressBar.visibility = View.GONE
                                        setSignUpButtonEnabled(true)
                                        onSignUpResult(true)
                                    }
                                    .addOnFailureListener { e ->
                                        Toast.makeText(
                                            this,
                                            "Profile updated but failed to save extra data: ${e.message}",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        binding.progressBar.visibility = View.GONE
                                        setSignUpButtonEnabled(true)
                                        onSignUpResult(true)
                                    }
                            } else {
                                // Handle profile update failure
                                binding.progressBar.visibility = View.GONE
                                setSignUpButtonEnabled(true)
                                Toast.makeText(
                                    this,
                                    "Sign up succeeded but profile update failed: ${profileTask.exception?.message}",
                                    Toast.LENGTH_SHORT
                                ).show()
                                onSignUpResult(false)
                            }
                        }
                } else {
                    // Handle sign up failure
                    binding.progressBar.visibility = View.GONE
                    setSignUpButtonEnabled(true)
                    Toast.makeText(
                        this,
                        "Sign up failed: ${task.exception?.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                    onSignUpResult(false)
                }
            }
    }



    private fun isEmailValid(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun showNoInternetDialog() {
        AlertDialog.Builder(this)
            .setTitle("No Internet Connection")
            .setMessage("Please check your internet connection and try again.")
            .setPositiveButton("Retry") { dialog, _ ->
                dialog.dismiss()
                // Recheck connectivity when "Retry" is pressed.
                if (!isInternetAvailable(this)) {
                    Toast.makeText(this, "Still no internet connection.", Toast.LENGTH_SHORT).show()
                    showNoInternetDialog()
                }
            }
            .setNegativeButton("Close") { dialog, _ ->
                dialog.dismiss()
                // Optionally, close the app if there is no connection.
                finish()
            }
            .setCancelable(false)
            .create()
            .show()
    }

    private fun isValidName(name: String): Boolean {
        val nameRegex = "^[\\p{L}\\s'.-]{3,30}$"
        return name.matches(nameRegex.toRegex())
    }

    private fun isInternetAvailable(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
        return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }

    private val pickAccountLauncher: ActivityResultLauncher<android.content.Intent> =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            binding.progressBar.visibility = View.GONE
            if (result.resultCode == Activity.RESULT_OK) {
                val accountName = result.data?.getStringExtra(AccountManager.KEY_ACCOUNT_NAME)
                accountName?.let {
                    binding.etEmail.setText(it)
                }
            } else {
                Toast.makeText(this, "Account selection canceled.", Toast.LENGTH_SHORT).show()
            }
        }

}