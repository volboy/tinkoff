package com.volboy.course_project.presentation.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.volboy.course_project.R
import com.volboy.course_project.databinding.FragmentMainBinding
import com.volboy.course_project.presentation.profile.MvpProfileFragment
import com.volboy.course_project.presentation.users.MvpUsersFragment

class MainFragment : Fragment() {
    private lateinit var binding: FragmentMainBinding
    private lateinit var transaction: FragmentTransaction
    private val channelsFragment = ChannelsFragment()
    private val peopleFragment = MvpUsersFragment()
    private val profileFragment = MvpProfileFragment()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
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
        transaction = requireActivity().supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container, fragment)
        transaction.commit()
    }
}
