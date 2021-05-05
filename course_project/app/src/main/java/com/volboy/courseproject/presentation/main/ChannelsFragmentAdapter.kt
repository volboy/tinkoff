package com.volboy.courseproject.presentation.main

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.volboy.courseproject.presentation.streams.allstreams.MvpStreamsFragment
import com.volboy.courseproject.presentation.streams.mystreams.MvpSubscribedFragment

class ChannelsFragmentAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> MvpSubscribedFragment()
            1 -> MvpStreamsFragment()
            else -> MvpSubscribedFragment()
        }
    }
}