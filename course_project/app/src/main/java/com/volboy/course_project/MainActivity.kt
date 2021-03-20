package com.volboy.course_project

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.volboy.course_project.R.id
import com.volboy.course_project.customviews.EmojiView
import com.volboy.course_project.customviews.FlexBoxLayout
import com.volboy.course_project.customviews.dpToPx
import com.volboy.course_project.databinding.ActivityMainBinding
import com.volboy.course_project.message_recycler_view.*
import com.volboy.course_project.model.LoaderMessage
import com.volboy.course_project.model.Reaction
import com.volboy.course_project.ui.ChannelsFragment
import com.volboy.course_project.ui.EmojiBottomFragment
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

