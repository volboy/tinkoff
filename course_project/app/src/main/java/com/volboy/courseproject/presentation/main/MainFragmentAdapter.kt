package com.volboy.courseproject.presentation.main

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.volboy.courseproject.presentation.profile.MvpProfileFragment
import com.volboy.courseproject.presentation.users.MvpUsersFragment

class MainFragmentAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = COUNT_OF_PAGE_IN_BOTTOM_NAVIGATION

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> ChannelsFragment()
            1 -> MvpUsersFragment()
            2 -> MvpProfileFragment()
            else -> ChannelsFragment()
        }
    }

    private companion object {
        const val COUNT_OF_PAGE_IN_BOTTOM_NAVIGATION = 3
    }
}