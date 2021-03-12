package com.volboy.hw_2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.volboy.hw_2.databinding.ActivityMainBinding
import com.volboy.hw_2.message_recycler_view.MessageAdapter
import com.volboy.hw_2.model.Messege

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val messages = listOf(
            Messege(1, resources.getString(R.string.header_str), resources.getString(R.string.message_str), true, "12 Мар"),
            Messege(2, "You", "Привет", false,"12 Мар"),
            Messege(3, "You", "Чем могу помочь?", false, "12 Мар"),
            Messege(4,  resources.getString(R.string.header_str), "Хочу играть в хоккей", true, "13 Мар")
        )

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.recyclerMessage.layoutManager = LinearLayoutManager(applicationContext)
        binding.recyclerMessage.adapter = MessageAdapter(messages)

    }

    fun clickEmojiiView(view: View) {
        //view.isSelected = !view.isSelected
    }
}