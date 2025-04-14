package com.example.ailawyer

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ailawyer.adapters.Review
import com.example.ailawyer.adapters.ReviewAdapter
import com.example.ailawyer.databinding.ActivityIntroBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class IntroActivity : AppCompatActivity() {

    private lateinit var binding: ActivityIntroBinding
    private lateinit var bottomNavigationView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityIntroBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Disable the default system window insets handling.
        WindowCompat.setDecorFitsSystemWindows(window, false)

        // Adjust padding for system bars.
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { view, insets ->
            val systemBarsInsets = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.updatePadding(top = systemBarsInsets.top, bottom = systemBarsInsets.bottom)
            insets
        }

        val lawyerId = intent.getStringExtra("LawyerId") ?: ""
        val lawyerName = intent.getStringExtra("Name") ?: "Lawyer"
        val rating = intent.getFloatExtra("Rating", 0f)
        val reviewCount = intent.getIntExtra("ReviewCount", 0)
        val description = intent.getStringExtra("Description") ?: getRandomLawyerDescription()
        val imageKey = intent.getStringExtra("ImageKey") ?: "a" // fallback key

        binding.userName.text = lawyerName
        binding.ratingBar.rating = rating
        binding.ratingText.text = "$rating out of $reviewCount reviews"
        binding.descriptionText.text = getRandomLawyerDescription()

        val context = binding.profileImage.context
        val resId = context.resources.getIdentifier(imageKey, "drawable", context.packageName)
        if (resId != 0) {
            binding.profileImage.setImageResource(resId)
        }

        val reviews = listOf(
            Review("Areeb Khan", 4.5f, "Very polite and cooperative. Helped with legal issues efficiently.",'a'),
            Review("Zain Malik", 5.0f, "Highly recommended! Got my case resolved quickly.",'b'),
            Review("Sarmad Ahmed", 4.0f, "Good communication and affordable service.",'c'),
            Review("Ali Raza", 3.5f, "A bit slow but still resolved everything.",'d'),
            Review("Mehad Javed", 4.8f, "Outstanding professionalism and support.",'f')
        )

        val reviewAdapter = ReviewAdapter(reviews)
        binding.reviewsRecyclerView.adapter = reviewAdapter
        binding.reviewsRecyclerView.layoutManager = LinearLayoutManager(this)


        binding.sendMessageButton.setOnClickListener {
            val intent = Intent(this@IntroActivity, ChatscreenActivity::class.java)
            intent.putExtra("LawyerName", lawyerName)
            intent.putExtra("LawyerId", lawyerId)
            intent.putExtra("ImageKey", imageKey)
            startActivity(intent)
        }

//        bottomNavigationView = binding.bottomNavigationView
//
//        // This dummy item will be selected by default.
//        // Handle bottom navigation item clicks.
//        bottomNavigationView.setOnNavigationItemSelectedListener { menuItem ->
//            // Ignore the dummy item
//            if (menuItem.itemId == R.id.dummy) {
//                return@setOnNavigationItemSelectedListener false
//            }
//            menuItem.isChecked = true
//
//            when (menuItem.itemId) {
//                R.id.navigation_setting -> startActivity(Intent(this, Settings::class.java))
//                R.id.navigation_Chats -> startActivity(
//                    Intent(
//                        this,
//                        LawyerScreenActivity::class.java
//                    )
//                )
//
//                R.id.navigation_location -> startActivity(Intent(this, CityActivity::class.java))
//            }
//            true
//        }
    }

    fun getRandomLawyerDescription(): String {
        val descriptions = listOf(
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
