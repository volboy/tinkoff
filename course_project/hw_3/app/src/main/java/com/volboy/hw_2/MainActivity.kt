package com.volboy.hw_2

import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.volboy.hw_2.databinding.ActivityMainBinding
import com.volboy.hw_2.message_recycler_view.*
import com.volboy.hw_2.model.LoaderMessage

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        val longClickListener: (View) -> Boolean = {
            Toast.makeText(this, "Wow", Toast.LENGTH_LONG).show()
            true
        }
        val holderFactory = MessageHolderFactory(longClickListener)
        val messageAdapter = MessagesAdapter<ViewTyped>(holderFactory)
        val loaderMessage = LoaderMessage() // типа загрузчик сообщений
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.recyclerMessage.layoutManager = LinearLayoutManager(applicationContext)
        messageAdapter.items = loaderMessage.remoteMessage()
        binding.recyclerMessage.adapter = messageAdapter
        binding.recyclerMessage.scrollToPosition(messageAdapter.items.size - 1)

        binding.messageBtn.setOnClickListener {
            loaderMessage.addMessage(getNewMessage())
            messageAdapter.items = loaderMessage.addMessage(getNewMessage())
            messageAdapter.notifyItemChanged(messageAdapter.items.size)
            binding.recyclerMessage.scrollToPosition(messageAdapter.items.size-1)
        }

        binding.messageBox.setOnKeyListener(View.OnKeyListener { _, keyCode, event ->
            if (binding.messageBox.text.isNotEmpty()) {
                binding.messageBtn.setImageResource(R.drawable.ic_send_message)
            } else {
                binding.messageBtn.setImageResource(R.drawable.ic_add_message)
            }
            if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                getNewMessage()
                return@OnKeyListener true
            }
            false
        })
    }

    private fun getNewMessage(): String? {
        val newMessageText = binding.messageBox.text.toString()
        return if (newMessageText.isNotEmpty()) {
            binding.messageBox.text.clear()
            newMessageText
        } else {
            null
        }
    }
}