package com.volboy.course_project

import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import com.volboy.course_project.databinding.ActivityMainBinding
import com.volboy.course_project.message_recycler_view.ViewTyped
import com.volboy.course_project.ui.MainFragment
import com.volboy.course_project.ui.channel_fragments.tab_layout_fragments.TitleUi
import io.reactivex.Observable

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


