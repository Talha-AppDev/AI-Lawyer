package com.example.ailawyer

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import com.example.ailawyer.databinding.ActivityIntroBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class IntroActivity : AppCompatActivity() {
    lateinit var binding: ActivityIntroBinding
    private lateinit var bottomNavigationView: BottomNavigationView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityIntroBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Disable the default system window insets handling.
        WindowCompat.setDecorFitsSystemWindows(window, false)

        // Adjust the padding of the root view to account for system bars.
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { view, insets ->
            val systemBarsInsets = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.updatePadding(
                top = systemBarsInsets.top,
                bottom = systemBarsInsets.bottom
            )
            insets
        }

        binding.sendMessageButton.setOnClickListener {
            startActivity(Intent(this@IntroActivity, ChatscreenActivity::class.java))
        }

        bottomNavigationView = binding.bottomNavigationView

        // Handle bottom navigation item clicks
        bottomNavigationView.setOnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.navigation_setting -> startActivity(Intent(this, Settings::class.java))
                R.id.navigation_Chats -> startActivity(Intent(this, LawyerScreenActivity::class.java))
                R.id.navigation_location -> startActivity(Intent(this, CityActivity::class.java))
            }
            true
        }
    }

}
