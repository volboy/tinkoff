package com.volboy.courseproject.presentation.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import com.volboy.courseproject.R
import com.volboy.courseproject.databinding.FragmentChannelsBinding

class ChannelsFragment : Fragment() {
    private lateinit var binding: FragmentChannelsBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentChannelsBinding.inflate(inflater, container, false)
        (requireActivity() as AppCompatActivity).supportActionBar?.show()
        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        requireActivity().actionBar?.setDisplayShowTitleEnabled(false)
        val tabLayout = binding.tabs
        val viewPager = binding.viewPager
        val viewPagerAdapter = ChannelsFragmentAdapter(this)
        viewPager.adapter = viewPagerAdapter
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            when (position) {
                0 -> tab.text = resources.getString(R.string.tab_channels_1)
                1 -> tab.text = resources.getString(R.string.tab_channels_2)
            }
        }.attach()
    }
}