package com.example.ailawyer.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.ailawyer.R

class SelectedCity: Fragment(R.layout.fragment_selectcity)

class TypeFragAdapter(fragmentActivity: FragmentActivity): FragmentStateAdapter(fragmentActivity) {
    override fun getItemCount(): Int = 2
    override fun createFragment(position: Int): Fragment {
        return when(position)
        {
            0 -> SelectedCity()
            else -> throw IllegalStateException("Unexpected position $position")
        }
    }
}