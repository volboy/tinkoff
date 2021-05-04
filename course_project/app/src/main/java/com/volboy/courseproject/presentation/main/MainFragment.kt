package com.volboy.courseproject.presentation.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.volboy.courseproject.R
import com.volboy.courseproject.databinding.FragmentMainBinding

class MainFragment : Fragment() {
    private lateinit var binding: FragmentMainBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val viewPager = binding.viewPager
        val viewPagerAdapter = MainFragmentAdapter(this)
        viewPager.isUserInputEnabled = false
        viewPager.adapter = viewPagerAdapter
        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                when (position) {
                    0 -> binding.bottomNavigation.menu.findItem(R.id.channels).isChecked = true
                    1 -> binding.bottomNavigation.menu.findItem(R.id.people).isChecked = true
                    2 -> binding.bottomNavigation.menu.findItem(R.id.profile).isChecked = true
                }
            }
        })
        binding.bottomNavigation.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.channels -> {
                    viewPager.currentItem = 0
                    (requireActivity() as AppCompatActivity).supportActionBar?.show()
                }
                R.id.people -> {
                    viewPager.currentItem = 1
                    (requireActivity() as AppCompatActivity).supportActionBar?.show()
                }
                R.id.profile -> {
                    viewPager.currentItem = 2
                    (requireActivity() as AppCompatActivity).supportActionBar?.hide()
                }
            }
            true
        }
    }
}

