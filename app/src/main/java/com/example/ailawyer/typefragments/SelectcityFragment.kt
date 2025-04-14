package com.example.ailawyer.typefragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AdapterView
import androidx.fragment.app.Fragment
import com.example.ailawyer.CityResultActivity
import com.example.ailawyer.databinding.FragmentSelectcityBinding

class SelectcityFragment : Fragment() {

    private lateinit var binding: FragmentSelectcityBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSelectcityBinding.inflate(inflater, container, false)

        val cities = listOf(
            "Karachi", "Lahore", "Islamabad", "Rawalpindi"
        )

        binding.continueBTN.setOnClickListener {
            val intent = Intent(requireContext(), CityResultActivity::class.java)
            startActivity(intent)
        }

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
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }

        return binding.root
    }
}