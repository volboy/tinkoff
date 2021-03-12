package com.volboy.hw_2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.volboy.hw_2.databinding.ActivityMainBinding
import com.volboy.hw_2.message_recycler_view.MessageAdapter
import com.volboy.hw_2.model.Message

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var messages: MutableList<Message>
    private lateinit var newMessageText: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        messages = mutableListOf(
            Message(1, resources.getString(R.string.header_str), resources.getString(R.string.message_str), true, "12 Мар"),
            Message(2, "You", "Привет", false, "12 Мар"),
            Message(3, "You", "Чем могу помочь?", false, "12 Мар"),
            Message(4, resources.getString(R.string.header_str), "Хочу играть в хоккей", true, "13 Мар"),
            Message(5, "You", "Но ты не хоккеист", false, "13 Мар"),
            Message(6, resources.getString(R.string.header_str), "Я много тренировался играть в хоккей, и у меня хорошо получается", true, "14 Мар"),
            Message(7, "You", "Окей, тренировка в среду в 17.00", false, "14 Мар")
        )
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.recyclerMessage.layoutManager = LinearLayoutManager(applicationContext)
        binding.recyclerMessage.adapter = MessageAdapter(messages)
        binding.recyclerMessage.scrollToPosition(messages.size - 1)
        binding.messageBtn.setOnClickListener {
            sendOutMessage()
        }
        binding.messageBox.setOnKeyListener(View.OnKeyListener { _, keyCode, event ->
            if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER && binding.messageBox.text.isNotEmpty()) {
                sendOutMessage()
                return@OnKeyListener true
            }
            false
        })
    }

    private fun sendOutMessage() {
        if (binding.messageBox.text.isNotEmpty()) return
        newMessageText = binding.messageBox.text.toString()
        messages.add(Message(8, "You", newMessageText, false, "14 Мар"))
        binding.recyclerMessage.adapter?.notifyItemInserted(messages.size - 1)
        binding.recyclerMessage.scrollToPosition(messages.size - 1)
        binding.messageBox.text.clear()
    }


    fun clickEmojiiView(view: View) {
        //view.isSelected = !view.isSelected
    }
}