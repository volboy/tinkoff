package com.volboy.course_project.ui.channel_fragments

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.volboy.course_project.ui.channel_fragments.tab_layout_fragments.StreamsFragment
import com.volboy.course_project.ui.channel_fragments.tab_layout_fragments.SubscribedFragment

class ChannelsFragmentAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> SubscribedFragment()
            1 -> StreamsFragment()
            else -> SubscribedFragment()
        }
    }
}