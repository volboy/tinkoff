package com.volboy.course_project

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.volboy.course_project.databinding.ActivityMainBinding
import com.volboy.course_project.presentation.main.MainFragment

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val mainFragment = MainFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val searchToolbar = binding.searchToolbar
        setSupportActionBar(searchToolbar)
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container, mainFragment)
        transaction.commit()
        setContentView(binding.root)
    }
}


