package com.volboy.hw_2

import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.volboy.hw_2.databinding.ActivityMainBinding
import com.volboy.hw_2.message_recycler_view.MessageAdapter
import com.volboy.hw_2.model.Message

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var newMessageText: String
    private lateinit var messages: MutableList<Message>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        messages = convertMessage(loadMessages()).toMutableList()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.recyclerMessage.layoutManager = LinearLayoutManager(applicationContext)
        binding.recyclerMessage.adapter = MessageAdapter(messages)
        binding.recyclerMessage.scrollToPosition(messages.size - 1)
        binding.messageBtn.setOnClickListener {
            sendOutMessage()
        }
        binding.messageBox.setOnKeyListener(View.OnKeyListener { _, keyCode, event ->
            if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                sendOutMessage()
                return@OnKeyListener true
            }
            false
        })
    }

    private fun sendOutMessage() {
        if (binding.messageBox.text.isBlank()) return
        newMessageText = binding.messageBox.text.toString()
        val newMessage = Message(8, "You", newMessageText, 0, "14 Мар")
        messages.add(newMessage)
        binding.recyclerMessage.adapter?.notifyItemInserted(messages.size-1)
        binding.recyclerMessage.scrollToPosition(messages.size - 1)
        binding.messageBox.text.clear()
    }

    private fun convertMessage(messages: MutableList<Message>): List<Message> {
        val messageByDate = messages.groupBy { it.date }
        return messageByDate.flatMap { (key, values) ->
            listOf(Message(0, "null", "null", 2, key)) + values
        }
    }

    private fun loadMessages(): MutableList<Message> {
        return mutableListOf(
            Message(1, resources.getString(R.string.header_str), resources.getString(R.string.message_str), 1, "12 Мар"),
            Message(2, "You", "Привет", 0, "12 Мар"),
            Message(3, "You", "Чем могу помочь?", 0, "12 Мар"),
            Message(4, resources.getString(R.string.header_str), "Хочу играть в хоккей", 1, "13 Мар"),
            Message(5, "You", "Но ты не хоккеист", 0, "13 Мар"),
            Message(6, resources.getString(R.string.header_str), "Я много тренировался играть в хоккей, и у меня хорошо получается", 1, "14 Мар"),
            Message(7, "You", "Окей, тренировка в среду в 17.00", 0, "14 Мар")
        )
    }


    fun clickEmojiiView(view: View) {
        //view.isSelected = !view.isSelected
    }


}