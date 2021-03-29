package com.volboy.course_project

import android.os.Bundle
import android.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity
import com.volboy.course_project.databinding.ActivityMainBinding
import com.volboy.course_project.ui.MainFragment

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val mainFragment=MainFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.container, mainFragment)
            transaction.commit()
        val searchToolbar=binding.searchToolbar
        setSupportActionBar(searchToolbar)
        setContentView(binding.root)
    }
}

