package com.volboy.course_project

import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.volboy.course_project.databinding.ActivityMainBinding
import com.volboy.course_project.message_recycler_view.*
import com.volboy.course_project.model.LoaderMessage

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        val longClickListener: (View) -> Boolean = {
            val dialog = BottomSheetDialog(this)
            dialog.setContentView(R.layout.bottom_emoji_dialog)
            dialog.show()

            true
        }
        val holderFactory = MessageHolderFactory(longClickListener)
        val messageAdapter = MessagesAdapter<ViewTyped>(holderFactory)
        val loaderMessage = LoaderMessage() // типа загрузчик сообщений
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.fragmentMessages.recyclerMessage.layoutManager = LinearLayoutManager(applicationContext)
        messageAdapter.items = loaderMessage.remoteMessage()
        binding.fragmentMessages.recyclerMessage.adapter = messageAdapter
        binding.fragmentMessages.recyclerMessage.scrollToPosition(messageAdapter.items.size - 1)

        binding.fragmentMessages.messageBtn.setOnClickListener {
            loaderMessage.addMessage(getNewMessage())
            messageAdapter.items = loaderMessage.addMessage(getNewMessage())
            messageAdapter.notifyItemChanged(messageAdapter.items.size)
            binding.fragmentMessages.recyclerMessage.scrollToPosition(messageAdapter.items.size-1)
        }

        binding.fragmentMessages.messageBox.setOnKeyListener(View.OnKeyListener { _, keyCode, event ->
            if (binding.fragmentMessages.messageBox.text.isNotBlank()) {
                binding.fragmentMessages.messageBtn.setImageResource(R.drawable.ic_send_message)
            } else {
                binding.fragmentMessages.messageBtn.setImageResource(R.drawable.ic_add_message)
            }
            if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                getNewMessage()
                return@OnKeyListener true
            }
            false
        })
    }

    private fun getNewMessage(): String? {
        val newMessageText = binding.fragmentMessages.messageBox.text.toString()
        return if (newMessageText.isNotEmpty()) {
            binding.fragmentMessages.messageBox.text.clear()
            binding.fragmentMessages.messageBtn.setImageResource(R.drawable.ic_add_message)
            newMessageText
        } else {
            null
        }
    }
}