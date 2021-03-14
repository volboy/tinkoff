package com.volboy.hw_2

import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.volboy.hw_2.databinding.ActivityMainBinding
import com.volboy.hw_2.message_recycler_view.*
import com.volboy.hw_2.model.LoaderMessage
import com.volboy.hw_2.model.Message

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var newMessageText: String
    private val holderFactory=MessageHolderFactory()
    private val messageAdapter = MessagesAdapter<ViewTyped>(holderFactory)

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.recyclerMessage.layoutManager = LinearLayoutManager(applicationContext)
        messageAdapter.items= LoaderMessage().remoteMessage() // типа грузим сообщения
        binding.recyclerMessage.adapter=messageAdapter
        binding.recyclerMessage.scrollToPosition(messageAdapter.items.size-1)

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

        binding.messageBox.text.clear()
    }

    fun clickEmojiiView(view: View) {
        //view.isSelected = !view.isSelected
    }


}