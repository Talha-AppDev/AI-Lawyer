package com.example.ailawyer

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ailawyer.adapters.Review
import com.example.ailawyer.adapters.ReviewAdapter
import com.example.ailawyer.databinding.ActivityIntroBinding
import com.google.firebase.auth.FirebaseAuth

class IntroActivity : AppCompatActivity() {

    private lateinit var binding: ActivityIntroBinding
    private val TAG = "IntroActivity"

    private lateinit var LawyerId: String
    private lateinit var LawyerName: String
    private var LawyerRating: Float = 0f
    private var LawyerReviewCount: Int = 0
    private lateinit var LawyerDescription: String
    private lateinit var LawyerImageKey: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityIntroBinding.inflate(layoutInflater)
        setContentView(binding.root)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { view, insets ->
            val bars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.updatePadding(top = bars.top, bottom = bars.bottom)
            insets
        }

        // Read extras
        LawyerId = intent.getStringExtra("LawyerId") ?: ""
        LawyerName = intent.getStringExtra("LawyerName") ?: "Lawyer"
        LawyerRating = intent.getFloatExtra("LawyerRating", 0f)
        LawyerReviewCount = intent.getIntExtra("LawyerReviewCount", 0)
        LawyerDescription = getRandomLawyerDescription()
        LawyerImageKey = intent.getStringExtra("LawyerImageKey") ?: "a"

        Log.d(TAG, "Loaded lawyer: $LawyerId, name=$LawyerName")

        // Populate UI
        binding.userName.text = LawyerName
        binding.ratingBar.rating = LawyerRating
        binding.ratingText.text = "$LawyerRating out of ${LawyerReviewCount} reviews"
        binding.descriptionText.text = LawyerDescription

        // Load profile image
        val resId = resources.getIdentifier(LawyerImageKey, "drawable", packageName)
        if (resId != 0) binding.profileImage.setImageResource(resId)

        // Dummy reviews list (client sees these)
        val reviews = listOf(
            Review("Areeb Khan", 4.5f, "Very polite and cooperative. Helped with legal issues efficiently.", 'a'),
            Review("Zain Malik", 5.0f, "Highly recommended! Got my case resolved quickly.", 'b'),
            Review("Sarmad Ahmed", 4.0f, "Good communication and affordable service.", 'c'),
            Review("Ali Raza", 3.5f, "A bit slow but still resolved everything.", 'd'),
            Review("Mehad Javed", 4.8f, "Outstanding professionalism and support.", 'f')
        )
        binding.reviewsRecyclerView.adapter = ReviewAdapter(reviews)
        binding.reviewsRecyclerView.layoutManager = LinearLayoutManager(this)

        // Send message to chat with this lawyer
        binding.sendMessageButton.setOnClickListener {
            val currentUserId = FirebaseAuth.getInstance().currentUser?.uid ?: ""
            Intent(this, ChatscreenActivity::class.java).apply {
                putExtra("LawyerId", LawyerId)
                putExtra("LawyerName", LawyerName)
                putExtra("LawyerImageKey", LawyerImageKey)
                startActivity(this)
            }
        }
    }

    fun getRandomLawyerDescription(): String {
        val descriptions = listOf (
            "With over 18 years of courtroom experience, I have successfully represented clients in high-profile criminal cases, including white-collar ...",
            "As a seasoned family law attorney, I understand the emotional toll legal disputes can take. Whether you're dealing wit...",
            "I specialize in corporate and business law, working closely with startups, SMEs, and large corporations. From company formation to contrac...",
            "My firm focuses on civil litigation, helping individuals and organizations resolve disputes related to contracts, ...",
            "With a background in constitutional law and public interest advocacy, I champion human rights cases and en...",
            "Having assisted countless clients in immigration matters, I provide reliable legal counsel for visa applications, citiz...",
            "My expertise in real estate law ensures smooth transactions and strong legal protection in matters such...",
            "As a tech-savvy legal expert, I focus on digital law and cybercrime. I represent clients in cases involving online f...",
            "With a passion for labor law and social justice, I help employees and employers resolve dispute...",
            "A trusted legal advisor for entrepreneurs and creatives, I offer legal guidance on brand protecti..."
        )
        return descriptions.random()
    }
}
