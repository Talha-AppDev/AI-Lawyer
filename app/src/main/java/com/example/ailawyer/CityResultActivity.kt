package com.example.ailawyer

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ailawyer.databinding.ActivityCityResultBinding
import com.example.ailawyer.databinding.LawyerAttributeBinding
import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import kotlin.random.Random

class CityResultActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCityResultBinding
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var lawyerAdapter: LawyerAdapter
    private lateinit var clientAdapter: ClientAdapter
    private val db = FirebaseFirestore.getInstance()

    // Data model for Lawyer
    data class Lawyer(
        val id: String,
        val displayName: String,
        val rating: Float = (Random.nextDouble(3.0, 5.0) * 10).toInt() / 10f,
        val reviewCount: Int = Random.nextInt(10, 100),
        val description: String = getRandomDescription(),
        val imageKey: String = getRandomImageKey()
    )

    // Data model for Client
    data class Client(
        val id: String = "",
        val name: String = "",
        val type: String = "Client",
        val lastMessage: String = "",
        val gmail: String = "",
        val timestamp: Long = 0L,
        val imageKey: String = ""
    )

    // Adapter for Lawyers (client sees lawyers)
    inner class LawyerAdapter(private val lawyers: MutableList<Lawyer>) :
        RecyclerView.Adapter<LawyerAdapter.LawyerViewHolder>() {

        inner class LawyerViewHolder(val binding: LawyerAttributeBinding) :
            RecyclerView.ViewHolder(binding.root)

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LawyerViewHolder {
            val itemBinding = LawyerAttributeBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
            return LawyerViewHolder(itemBinding)
        }

        override fun onBindViewHolder(holder: LawyerViewHolder, position: Int) {
            val lawyer = lawyers[position]
            holder.binding.userName.text = lawyer.displayName
            holder.binding.reviews.text = "${lawyer.rating} out of ${lawyer.reviewCount} reviews"
            holder.binding.ratingBar.rating = lawyer.rating
            holder.binding.description.text = lawyer.description

            val imageRes = when (lawyer.imageKey.lowercase()) {
                "a" -> R.drawable.a
                "b" -> R.drawable.b
                "c" -> R.drawable.c
                "e" -> R.drawable.e
                "f" -> R.drawable.f
                else -> R.drawable.a
            }
            holder.binding.profileImage.setImageResource(imageRes)

            holder.itemView.setOnClickListener {
                // Launch ChatscreenActivity as client chatting to this lawyer
                Intent(this@CityResultActivity, IntroActivity::class.java).apply {
                    putExtra("LawyerId", lawyer.id)
                    putExtra("LawyerName", lawyer.displayName)
                    putExtra("LawyerRating", lawyer.rating)
                    putExtra("LawyerReviewCount", lawyer.reviewCount)
                    putExtra("LawyerDescription", lawyer.description)
                    putExtra("LawyerImageKey", lawyer.imageKey)
                    startActivity(this)
                }
            }
        }

        override fun getItemCount(): Int = lawyers.size

        fun updateData(newLawyers: List<Lawyer>) {
            lawyers.clear()
            lawyers.addAll(newLawyers)
            notifyDataSetChanged()
        }
    }

    // Adapter for Clients (lawyer sees clients)
    inner class ClientAdapter(private val clients: MutableList<Client>) :
        RecyclerView.Adapter<ClientAdapter.ClientViewHolder>() {

        inner class ClientViewHolder(val binding: LawyerAttributeBinding) :
            RecyclerView.ViewHolder(binding.root)

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClientViewHolder {
            val itemBinding = LawyerAttributeBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
            return ClientViewHolder(itemBinding)
        }

        override fun onBindViewHolder(holder: ClientViewHolder, position: Int) {
            val client = clients[position]
            holder.binding.userName.text = client.name
            holder.binding.description.text = client.gmail
            holder.binding.reviews.visibility = View.GONE
            holder.binding.ratingBar.visibility = View.GONE

            val imageRes = when (client.imageKey.lowercase()) {
                "a" -> R.drawable.a
                "b" -> R.drawable.b
                "c" -> R.drawable.c
                "e" -> R.drawable.e
                "f" -> R.drawable.f
                else -> R.drawable.a
            }
            holder.binding.profileImage.setImageResource(imageRes)

            holder.itemView.setOnClickListener {
                // Launch ChatscreenActivity as lawyer chatting to this client
                Intent(this@CityResultActivity, ChatscreenActivity::class.java).apply {
                    putExtra("ClientId", client.id)
                    putExtra("ClientName", client.name)
                    putExtra("ClientImageKey", client.imageKey)
                    startActivity(this)
                }
            }
        }

        override fun getItemCount(): Int = clients.size

        fun updateData(newClients: List<Client>) {
            clients.clear()
            clients.addAll(newClients)
            notifyDataSetChanged()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        drawerLayout = binding.drawerLayout
        binding.recyclerView.layoutManager = LinearLayoutManager(this)

        binding.bottomNavigationView.setOnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.navigation_setting -> startActivity(Intent(this, SettingsActivity::class.java))
                R.id.navigation_Chats -> startActivity(Intent(this, LawyerScreenActivity::class.java))
                R.id.navigation_location -> startActivity(Intent(this, CityActivity::class.java))
            }
            if (drawerLayout.isDrawerOpen(GravityCompat.START)) drawerLayout.closeDrawer(GravityCompat.START)
            true
        }

        val userType = getSharedPreferences("UserPrefs", MODE_PRIVATE)
            .getString("userType", null)
            ?.lowercase()

        if (userType == "client") {
            binding.toolbar.text = "Lawyers"
            binding.progressBar.visibility = View.VISIBLE
            lawyerAdapter = LawyerAdapter(mutableListOf())
            binding.recyclerView.adapter = lawyerAdapter
            fetchLawyerData()

        } else if (userType == "lawyer") {
            binding.toolbar.text = "Clients"
            binding.progressBar.visibility = View.VISIBLE
            clientAdapter = ClientAdapter(mutableListOf())
            binding.recyclerView.adapter = clientAdapter

            val lawyerId = FirebaseAuth.getInstance().currentUser?.uid
            if (lawyerId != null) {
                fetchChatParticipants(lawyerId) { clientIds ->
                    binding.progressBar.visibility = View.GONE
                    if (clientIds.isNotEmpty()) {
                        binding.progressBar.visibility = View.VISIBLE
                        fetchClientDetails(clientIds)
                    } else {
                        showNoClientsAlert(this)
                    }
                }
            } else {
                binding.progressBar.visibility = View.GONE
                Log.e("CityResultActivity", "No logged-in lawyer ID found.")
            }
        }
    }

    private fun fetchChatParticipants(userId: String, callback: (List<String>) -> Unit) {
        db.collection("chats").get()
            .addOnSuccessListener { snap ->
                if (snap.isEmpty) return@addOnSuccessListener callback(emptyList())

                val ids = snap.documents.mapNotNull { doc ->
                    val parts = doc.id.split("_")
                    if (parts.size == 2 && parts.contains(userId)) {
                        parts.first { it != userId }
                    } else null
                }
                callback(ids.distinct())
            }
            .addOnFailureListener { e ->
                Log.e("CityResultActivity", "Error fetching chats", e)
                callback(emptyList())
            }
    }

    private fun fetchClientDetails(clientIds: List<String>) {
        val clientList = mutableListOf<Client>()
        val tasks = clientIds.map { id -> db.collection("clients").document(id).get() }
        Tasks.whenAllSuccess<DocumentSnapshot>(tasks)
            .addOnSuccessListener { snapshots ->
                snapshots.forEach { doc ->
                    if (doc.exists()) {
                        val id = doc.id
                        val name = doc.getString("displayName") ?: doc.getString("name") ?: "Unknown"
                        val email = doc.getString("email") ?: "Unknown"
                        clientList.add(Client(id = id, name = name, gmail = email, imageKey = getRandomImageKey()))
                    }
                }
                binding.progressBar.visibility = View.GONE
                clientAdapter.updateData(clientList)
            }
            .addOnFailureListener { e ->
                Log.e("CityResultActivity", "Failed fetching clients", e)
                binding.progressBar.visibility = View.GONE
                showNoClientsAlert(this)
            }
    }

    private fun fetchLawyerData() {
        db.collection("lawyers").get()
            .addOnSuccessListener { snap ->
                val list = snap.documents.mapNotNull { doc ->
                    doc.getString("displayName")?.let { Lawyer(id = doc.id, displayName = it) }
                }
                binding.progressBar.visibility = View.GONE
                lawyerAdapter.updateData(list)
            }
            .addOnFailureListener { e ->
                Log.e("CityResultActivity", "Failed to fetch lawyers", e)
                binding.progressBar.visibility = View.GONE
            }
    }

    private fun showNoClientsAlert(context: Context) {
        AlertDialog.Builder(context)
            .setTitle("No clients at the moment.")
            .setMessage("Clients will be shown to you when they contact you.")
            .setPositiveButton("OK") { d, _ -> d.dismiss() }
            .setCancelable(false)
            .show()
    }

    override fun onBackPressed() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    companion object {
        private fun getRandomDescription(): String =
            listOf(
                "I will help you in all your legal complexities. Feel free to contact me.",
                "Experienced lawyer here to assist with any legal matters you have.",
                "Your trusted legal advisor for family, civil, and criminal law.",
                "Let's make the law simple together. Consult today.",
                "Providing expert legal advice at your convenience."
            ).random()

        private fun getRandomImageKey(): String = listOf("a", "b", "c", "e", "f").random()
    }
}
