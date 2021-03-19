package com.volboy.course_project

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.volboy.course_project.customviews.EmojiView
import com.volboy.course_project.customviews.FlexBoxLayout
import com.volboy.course_project.customviews.dpToPx
import com.volboy.course_project.databinding.ActivityMainBinding
import com.volboy.course_project.message_recycler_view.*
import com.volboy.course_project.model.LoaderMessage
import com.volboy.course_project.model.Reaction

class MainActivity : AppCompatActivity(), EmojiBottomFragment.EmojiEventInterface {
    private lateinit var binding: ActivityMainBinding
    private lateinit var reactionsOfMessage: MutableList<Reaction>
    private lateinit var itemFromMessages: View

    override fun onCreate(savedInstanceState: Bundle?) {
        val emj = String(Character.toChars(0x1F60E))
        reactionsOfMessage = mutableListOf(Reaction(emj, "You", 1))

        val longClickListener: (View) -> Boolean = { view ->
            itemFromMessages=view
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
        binding.fragmentMessages.recyclerMessage.layoutManager = LinearLayoutManager(applicationContext)
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

    override fun emojiClickListener(emoji: String) {
        var r = reactionsOfMessage.firstOrNull { it.emoji == emoji }
        if (r == null) {
            reactionsOfMessage.add(Reaction(emoji, "You", 1))
        } else {
            if (r.usersId == "You") {
                r.count = r.count - 1
                if (r.count <= 0) {
                    r.count = 0
                }
            } else {
                r.count = r.count + 1
            }
        }
        val iterator = reactionsOfMessage.listIterator()
        while (iterator.hasNext()) {
            var item = iterator.next()
            if (item.count == 0) {
                iterator.remove()
            }
        }
        val flexBoxLayout: FlexBoxLayout = itemFromMessages.findViewById(R.id.flex_box_layout)
        val emojiView = EmojiView(flexBoxLayout.context)
        val layoutParams = FrameLayout.LayoutParams(applicationContext.dpToPx(45F), applicationContext.dpToPx(30F))
        layoutParams.rightMargin = applicationContext.dpToPx(10F)
        layoutParams.bottomMargin = applicationContext.dpToPx(7F)
        emojiView.setPadding(applicationContext.dpToPx(9F),applicationContext.dpToPx(4.8F),applicationContext.dpToPx(9F), applicationContext.dpToPx(4.8F))
        emojiView.text = "20"
        emojiView.emoji = emoji
        emojiView.setBackgroundResource(R.drawable.emodji_view_state)
        emojiView.layoutParams = layoutParams
        flexBoxLayout.addView(emojiView)
    }
}

