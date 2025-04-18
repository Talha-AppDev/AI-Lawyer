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

    private lateinit var lawyerId: String
    private lateinit var lawyerName: String
    private var rating: Float = 0f
    private var reviewCount: Int = 0
    private lateinit var description: String
    private lateinit var imageKey: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityIntroBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Disable default system windows insets
        WindowCompat.setDecorFitsSystemWindows(window, false)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { view, insets ->
            val bars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.updatePadding(top = bars.top, bottom = bars.bottom)
            insets
        }

        // Read extras
        lawyerId = intent.getStringExtra("LawyerId") ?: ""
        lawyerName = intent.getStringExtra("LawyerName") ?: intent.getStringExtra("Name") ?: "Lawyer"
        rating = intent.getFloatExtra("Rating", 0f)
        reviewCount = intent.getIntExtra("ReviewCount", 0)
        description = intent.getStringExtra("Description") ?: getRandomLawyerDescription()
        imageKey = intent.getStringExtra("ImageKey") ?: "a"

        Log.d(TAG, "Loaded lawyer: $lawyerId, name=$lawyerName, rating=$rating, reviews=$reviewCount")

        // Populate UI
        binding.userName.text = lawyerName
        binding.ratingBar.rating = rating
        binding.ratingText.text = "$rating out of $reviewCount reviews"
        binding.descriptionText.text = description

        // Load profile image
        val resId = resources.getIdentifier(imageKey, "drawable", packageName)
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
                putExtra("ClientId", currentUserId)
                putExtra("ClientName", "Me")
                putExtra("ClientImageKey", "")
                putExtra("LawyerId", lawyerId)
                putExtra("LawyerName", lawyerName)
                putExtra("LawyerImageKey", imageKey)
                startActivity(this)
            }
        }
    }

    fun getRandomLawyerDescription(): String {
        val descriptions = listOf (
            "With over 18 years of courtroom experience, I have successfully represented clients in high-profile criminal cases, including white-collar crimes, fraud, and serious felonies. My approach is strategic, thorough, and aggressive when necessary, always prioritizing your legal rights and a favorable outcome.",
            "As a seasoned family law attorney, I understand the emotional toll legal disputes can take. Whether you're dealing with divorce, child custody, or adoption, I provide personalized, compassionate representation that protects your interests and secures the best outcome for your family.",
            "I specialize in corporate and business law, working closely with startups, SMEs, and large corporations. From company formation to contract drafting, intellectual property protection, and litigation, I ensure your business operates smoothly within legal boundaries and is prepared for sustainable growth.",
            "My firm focuses on civil litigation, helping individuals and organizations resolve disputes related to contracts, property, torts, and more. I’m known for meticulous preparation, compelling arguments, and a commitment to achieving swift, cost-effective resolutions for my clients.",
            "With a background in constitutional law and public interest advocacy, I champion human rights cases and ensure my clients’ voices are heard in court. Whether it's discrimination, freedom of speech, or due process violations, I stand by those who seek justice and fairness.",
            "Having assisted countless clients in immigration matters, I provide reliable legal counsel for visa applications, citizenship, asylum, and deportation defense. I understand the complexities of immigration law and am committed to guiding clients through each step with care and clarity.",
            "My expertise in real estate law ensures smooth transactions and strong legal protection in matters such as property transfers, land disputes, commercial leases, and construction contracts. I prioritize transparency and minimize risk for buyers, sellers, and developers.",
            "As a tech-savvy legal expert, I focus on digital law and cybercrime. I represent clients in cases involving online fraud, privacy breaches, intellectual property theft, and tech startup compliance. I blend legal knowledge with technological awareness to protect your digital rights.",
            "With a passion for labor law and social justice, I help employees and employers resolve disputes involving wrongful termination, workplace harassment, wage issues, and employment contracts. I advocate strongly for fairness and lawful treatment in the workplace.",
            "A trusted legal advisor for entrepreneurs and creatives, I offer legal guidance on brand protection, contract negotiations, and regulatory compliance. I believe in empowering clients through knowledge, making the law approachable and a tool for achieving success."
        )
        return descriptions.random()
    }

}
