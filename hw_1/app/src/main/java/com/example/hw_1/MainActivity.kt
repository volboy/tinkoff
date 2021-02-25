package com.example.hw_1

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.hw_1.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private val requestCodeForActivityTwo = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding =
            DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)
        binding.btnToLoadActivity.setOnClickListener {
            val intent = Intent(this, ActivityTwo::class.java)
            startActivityForResult(intent, requestCodeForActivityTwo)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == requestCodeForActivityTwo) {
            if (resultCode == Activity.RESULT_OK) {
                val someData = data?.getStringExtra("SOME_DATA")
                Toast.makeText(this, someData, Toast.LENGTH_LONG).show()
            }
        }
    }
}