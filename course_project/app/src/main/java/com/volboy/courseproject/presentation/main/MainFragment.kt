package com.volboy.courseproject.presentation.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.volboy.courseproject.R
import com.volboy.courseproject.databinding.FragmentMainBinding
import com.volboy.courseproject.presentation.profile.MvpProfileFragment
import com.volboy.courseproject.presentation.users.MvpUsersFragment

class MainFragment : Fragment() {
    private lateinit var binding: FragmentMainBinding
    private val channelsFragment = ChannelsFragment()
    private val peopleFragment = MvpUsersFragment()
    private val profileFragment = MvpProfileFragment()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        replaceFragment(channelsFragment)
        binding.bottomNavigation.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.channels -> replaceFragment(channelsFragment)
                R.id.people -> replaceFragment(peopleFragment)
                R.id.profile -> replaceFragment(profileFragment)
            }
            true
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }
}
