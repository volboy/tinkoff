package com.volboy.course_project.presentation.main

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.volboy.course_project.presentation.streams.MvpStreamsFragment
import com.volboy.course_project.presentation.streams.MvpSubscribedFragment

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