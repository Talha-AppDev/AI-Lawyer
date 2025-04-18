package com.example.ailawyer.typefragments

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AdapterView
import androidx.fragment.app.Fragment
import com.example.ailawyer.CityResultActivity
import com.example.ailawyer.LoginActivity
import com.example.ailawyer.TypeActivity
import com.example.ailawyer.databinding.FragmentSelectcityBinding
import com.google.firebase.auth.FirebaseAuth

class SelectcityFragment : Fragment() {

    private lateinit var binding: FragmentSelectcityBinding
    private lateinit var sharedPref: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSelectcityBinding.inflate(inflater, container, false)

        // Initialize SharedPreferences
        sharedPref = requireContext().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)

        val cities = listOf(
            "Karachi", "Lahore", "Islamabad", "Rawalpindi"
        )

        // Logout button click listener
        binding.logoutBTN.setOnClickListener {
            // Clear shared preferences used for login
            with(sharedPref.edit()) {
                clear()   // Remove all saved entries; alternatively, remove specific keys
                apply()
            }

            // Sign out from Firebase Authentication
            FirebaseAuth.getInstance().signOut()

            // Redirect to LoginActivity and finish current activity
            val intent = Intent(requireContext(), TypeActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            requireActivity().finish()
        }

        // Continue button click listener
        binding.continueBTN.setOnClickListener {
            val intent = Intent(requireContext(), CityResultActivity::class.java)
            startActivity(intent)
        }

        // Setup spinner adapter for cities
        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            cities
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinner.adapter = adapter

        binding.spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val selectedCity = cities[position]
                // You can perform operations based on the selected city here.
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Optional: handle case when nothing is selected.
            }
        }

        return binding.root
    }
}
