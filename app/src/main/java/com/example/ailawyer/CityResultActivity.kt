package com.example.ailawyer

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ailawyer.databinding.ActivityCityResultBinding
import com.example.ailawyer.databinding.LawyerAttributeBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.firestore.FirebaseFirestore
import kotlin.random.Random

class CityResultActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCityResultBinding
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var bottomNavigationView: BottomNavigationView
    private val db = FirebaseFirestore.getInstance()

    private lateinit var lawyerAdapter: LawyerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        lawyerAdapter = LawyerAdapter(mutableListOf())
        binding.recyclerView.adapter = lawyerAdapter

        drawerLayout = binding.drawerLayout
        bottomNavigationView = binding.bottomNavigationView

        bottomNavigationView.setOnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.navigation_setting -> startActivity(Intent(this, Settings::class.java))
                R.id.navigation_Chats -> startActivity(Intent(this, LawyerScreenActivity::class.java))
                R.id.navigation_location -> startActivity(Intent(this, CityActivity::class.java))
            }
            if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                drawerLayout.closeDrawer(GravityCompat.START)
            }
            true
        }

        fetchLawyerData()
    }

    private fun fetchLawyerData() {
        db.collection("lawyers")
            .get()
            .addOnSuccessListener { snapshot ->
                val lawyerList = snapshot.documents.mapNotNull { doc ->
                    val name = doc.getString("displayName") ?: return@mapNotNull null
                    Lawyer(name)
                }
                lawyerAdapter.updateData(lawyerList)
            }
            .addOnFailureListener { e ->
                Log.e("CityResultActivity", "Failed to fetch lawyers", e)
            }
    }

    data class Lawyer(
        val displayName: String,
        val rating: Float = (Random.nextDouble(3.0, 5.0) * 10).toInt() / 10f,
        val reviewCount: Int = Random.nextInt(10, 100),
        val description: String = getRandomDescription(),
        val imageKey: String = getRandomImageKey()
    )

    inner class LawyerAdapter(private val lawyers: MutableList<Lawyer>) :
        RecyclerView.Adapter<LawyerAdapter.LawyerViewHolder>() {

        inner class LawyerViewHolder(val binding: LawyerAttributeBinding) :
            RecyclerView.ViewHolder(binding.root)

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LawyerViewHolder {
            val itemBinding = LawyerAttributeBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
            return LawyerViewHolder(itemBinding)
        }

        override fun onBindViewHolder(holder: LawyerViewHolder, position: Int) {
            val lawyer = lawyers[position]

            holder.binding.userName.text = lawyer.displayName
            holder.binding.reviews.text = "${lawyer.rating} out of ${lawyer.reviewCount} reviews"
            holder.binding.ratingBar.rating = lawyer.rating
            holder.binding.description.text = lawyer.description

            val imageRes = when (lawyer.imageKey) {
                "a" -> R.drawable.a
                "b" -> R.drawable.b
                "c" -> R.drawable.c
                "e" -> R.drawable.e
                "f" -> R.drawable.f
                else -> R.drawable.a
            }
            holder.binding.profileImage.setImageResource(imageRes)

            holder.itemView.setOnClickListener {
                val intent = Intent(this@CityResultActivity, IntroActivity::class.java)
                startActivity(intent)
            }
        }

        override fun getItemCount(): Int = lawyers.size

        fun updateData(newLawyers: List<Lawyer>) {
            lawyers.clear()
            lawyers.addAll(newLawyers)
            notifyDataSetChanged()
        }
    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    companion object {
        private fun getRandomDescription(): String {
            val descriptions = listOf(
                "I will help you in all your legal complexities. Feel free to contact me.",
                "Experienced lawyer here to assist with any legal matters you have.",
                "Your trusted legal advisor for family, civil, and criminal law.",
                "Let's make the law simple together. Consult today.",
                "Providing expert legal advice at your convenience."
            )
            return descriptions.random()
        }

        private fun getRandomImageKey(): String {
            val keys = listOf("a", "b", "c", "e", "f")
            return keys.random()
        }
    }
}