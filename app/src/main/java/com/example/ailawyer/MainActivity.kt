package com.example.ailawyer

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.example.ailawyer.databinding.ActivityMainBinding
import com.example.ailawyer.fragMA.ViewPagerAdapter
import com.google.android.material.tabs.TabLayoutMediator

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Set up view binding
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize ViewPager2 and TabLayout
        val viewPager = binding.viewPager
        val tabLayout = binding.tabLayout

        // Set the adapter for ViewPager2
        viewPager.adapter = ViewPagerAdapter(this)

        // Attach TabLayout with ViewPager2 using TabLayoutMediator
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> "Tab One"
                1 -> "Tab Two"
                2 -> "Tab Three"
                else -> null
            }
            if(position == 3)
                binding.pg.setText("Continue")
        }.attach()


        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                when (position) {
                    2 -> binding.pg.setText("Continue")
                    else -> binding.pg.setText("Swipe Left")
                }
            }
        })
    }
}
