package com.volboy.courseproject.presentation.main

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.volboy.courseproject.presentation.streams.allstreams.MvpStreamsFragment
import com.volboy.courseproject.presentation.streams.mystreams.MvpSubscribedFragment

class ChannelsFragmentAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = COUNT_OF_FRAGMENT_IN_TAB

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> MvpSubscribedFragment()
            1 -> MvpStreamsFragment()
            else -> MvpSubscribedFragment()
        }
    }

    private companion object {
        const val COUNT_OF_FRAGMENT_IN_TAB = 2
    }
}