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
    private lateinit var viewPagerAdapter: MainFragmentAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentMainBinding.inflate(inflater, container, false)
        viewPagerAdapter = MainFragmentAdapter(this)
        val viewPager = binding.viewPager
        viewPager.isUserInputEnabled = false
        viewPager.adapter = viewPagerAdapter
        savedInstanceState?.getInt("SAVED_STATE")?.let { viewPager.currentItem = it }
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
        return binding.root
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putInt("SAVED_STATE", binding.viewPager.currentItem)
        super.onSaveInstanceState(outState)

    }
}

