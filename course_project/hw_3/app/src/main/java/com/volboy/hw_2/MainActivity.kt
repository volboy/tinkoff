package com.volboy.hw_2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.volboy.hw_2.customviews.MessageViewGroup
import com.volboy.hw_2.databinding.ActivityMainBinding
import com.volboy.hw_2.databinding.ItemMessageBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val message= listOf("Привет", "Пока")
        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.recyclerMessage.layoutManager = LinearLayoutManager(applicationContext)
        binding.recyclerMessage.adapter = MessageAdapter(message)
    }
    fun clickEmojiiView(view: View) {
        //view.isSelected = !view.isSelected
    }
}