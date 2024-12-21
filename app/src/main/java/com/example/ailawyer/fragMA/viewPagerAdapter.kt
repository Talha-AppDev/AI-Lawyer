package com.example.ailawyer.fragMA

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.ailawyer.R

class fragment1 : Fragment(R.layout.fragment_1)
class fragment2 : Fragment(R.layout.fragment_2)
class fragment3 : Fragment(R.layout.fragment_3)

class ViewPagerAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {
    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> fragment1()
            1 -> fragment2()
            2 -> fragment3()
            else -> throw IllegalStateException("Unexpected position $position")
        }
    }
}
