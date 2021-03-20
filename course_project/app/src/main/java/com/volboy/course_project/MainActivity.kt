package com.volboy.course_project

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.volboy.course_project.R.id
import com.volboy.course_project.databinding.ActivityMainBinding
import com.volboy.course_project.ui.channel_fragment.ChannelsFragment
import com.volboy.course_project.ui.PeopleFragment
import com.volboy.course_project.ui.ProfileFragment

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val channelsFragment = ChannelsFragment()
    private val peopleFragment = PeopleFragment()
    private val profileFragment = ProfileFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        replaceFragment(channelsFragment)
        binding.bottomNavigation.setOnNavigationItemSelectedListener {
            when(it.itemId){
                id.channels -> replaceFragment(channelsFragment)
                id.people -> replaceFragment(peopleFragment)
                id.profile -> replaceFragment(profileFragment)
            }
            true
        }
        setContentView(binding.root)
    }

    private fun replaceFragment(fragment: Fragment) {
        if (fragment != null) {
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(id.fragment_container, fragment)
            transaction.commit()
        }
    }
}

