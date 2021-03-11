package com.volboy.hw_2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.volboy.hw_2.databinding.ActivityMainBinding
import com.volboy.hw_2.message_recycler_view.MessageAdapter
import com.volboy.hw_2.message_recycler_view.MessageItemDecoration

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val message = listOf("Привет", "Пока", "Куку", "Когда игра")
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.recyclerMessage.layoutManager = LinearLayoutManager(applicationContext)
        binding.recyclerMessage.adapter = MessageAdapter(message)
        val messageItemDecoration=MessageItemDecoration(resources.getString(R.string.data_divider_str), applicationContext)
        binding.recyclerMessage.addItemDecoration(messageItemDecoration)
    }

    fun clickEmojiiView(view: View) {
        //view.isSelected = !view.isSelected
    }
}