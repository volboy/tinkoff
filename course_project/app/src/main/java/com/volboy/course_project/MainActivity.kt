package com.volboy.course_project

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
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
            EmojiBottomFragment().apply {
                show(supportFragmentManager, EmojiBottomFragment.TAG)
            }
            true
        }
        val holderFactory = MessageHolderFactory(longClickListener)
        val messageAdapter = MessagesAdapter<ViewTyped>(holderFactory)
        val loaderMessage = LoaderMessage() // типа загрузчик сообщений
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.fragmentMessages.recyclerMessage.layoutManager =
            LinearLayoutManager(applicationContext)

        binding.fragmentMessages.recyclerMessage.adapter = messageAdapter
        messageAdapter.items = loaderMessage.remoteMessage()
        binding.fragmentMessages.recyclerMessage.scrollToPosition(messageAdapter.items.size - 1)

        binding.fragmentMessages.messageBtn.setOnClickListener {
            messageAdapter.items = loaderMessage.addMessage(getNewMessage())
            binding.fragmentMessages.recyclerMessage.scrollToPosition(messageAdapter.items.size - 1)
        }

        binding.fragmentMessages.messageBox.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (binding.fragmentMessages.messageBox.text.isNotEmpty()) {
                    binding.fragmentMessages.messageBtn.setImageResource(R.drawable.ic_send_message)
                }
            }
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