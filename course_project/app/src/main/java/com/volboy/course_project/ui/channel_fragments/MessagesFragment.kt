package com.volboy.course_project.ui.channel_fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.volboy.course_project.R
import com.volboy.course_project.customviews.EmojiView
import com.volboy.course_project.customviews.FlexBoxLayout
import com.volboy.course_project.customviews.dpToPx

import com.volboy.course_project.databinding.FragmentMessagesBinding
import com.volboy.course_project.message_recycler_view.CommonAdapter
import com.volboy.course_project.message_recycler_view.MessageHolderFactory
import com.volboy.course_project.message_recycler_view.TextUi
import com.volboy.course_project.message_recycler_view.ViewTyped
import com.volboy.course_project.model.Loader
import com.volboy.course_project.model.Message
import com.volboy.course_project.model.ObservableMessages
import com.volboy.course_project.model.Reaction
import com.volboy.course_project.ui.channel_fragments.tab_layout_fragments.SubscribedFragment

class MessagesFragment : Fragment(), EmojiBottomFragment.EmojiEventInterface {
    private lateinit var reactionsOfMessage: MutableList<Reaction>
    private lateinit var messages: MutableList<ViewTyped>
    private lateinit var itemFromMessages: View
    private lateinit var binding: FragmentMessagesBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentMessagesBinding.inflate(inflater, container, false)
        val emj = String(Character.toChars(0x1F60E))
        reactionsOfMessage = mutableListOf(Reaction(emj, "You", 1))
        val longClickListener: (View) -> Boolean = { view ->
            itemFromMessages = view
            val emojiBottomFragment = EmojiBottomFragment()
            emojiBottomFragment.show(parentFragmentManager, emojiBottomFragment.tag)
            true
        }
        val holderFactory = MessageHolderFactory(longClickListener)
        val messageAdapter = CommonAdapter<ViewTyped>(holderFactory)
        val loaderMessage = ObservableMessages() // типа загрузчик сообщений
        binding.recyclerMessage.adapter = messageAdapter

        val observableMessages = loaderMessage.getMessages().subscribe(
            { item ->
                messages = item as MutableList<ViewTyped>
                messageAdapter.items = messages
            },
            { error -> Snackbar.make(binding.root, error.toString(), Snackbar.LENGTH_LONG).show() })
        binding.recyclerMessage.scrollToPosition(messageAdapter.items.size - 1)
        binding.messageBtn.setOnClickListener {
            val newMessages = messages
            newMessages.add(addMessage(getNewMessage()))
            messageAdapter.items = newMessages
            binding.recyclerMessage.scrollToPosition(messageAdapter.items.size - 1)
        }
        binding.messageBox.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (binding.messageBox.text.isNotEmpty()) {
                    binding.messageBtn.setImageResource(R.drawable.ic_send_message)
                }
            }
        })
        val mActionBar = (requireActivity() as AppCompatActivity).supportActionBar;
        mActionBar?.hide()
        return binding.root
    }

    private fun getNewMessage(): String {
        val newMessageText = binding.messageBox.text.toString()
        return if (newMessageText.isNotEmpty()) {
            binding.messageBox.text.clear()
            binding.messageBtn.setImageResource(R.drawable.ic_add_message)
            newMessageText
        } else {
            ""
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
        val layoutParams = FrameLayout.LayoutParams(requireActivity().applicationContext.dpToPx(45F), requireActivity().applicationContext.dpToPx(30F))
        layoutParams.rightMargin = requireActivity().applicationContext.dpToPx(10F)
        layoutParams.bottomMargin = requireActivity().applicationContext.dpToPx(7F)
        emojiView.setPadding(
            requireActivity().applicationContext.dpToPx(9F), requireActivity().applicationContext.dpToPx(4.8F),
            requireActivity().applicationContext.dpToPx(9F), requireActivity().applicationContext.dpToPx(4.8F)
        )
        emojiView.text = "20"
        emojiView.emoji = emoji
        emojiView.setBackgroundResource(R.drawable.emoji_view_state)
        emojiView.layoutParams = layoutParams
        flexBoxLayout.addView(emojiView)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val topicName = requireArguments().getString(SubscribedFragment.ARG_TOPIC)
        val streamName = requireArguments().getString(SubscribedFragment.ARG_STREAM)
        binding.topicName.text = "Topic: #${topicName?.toLowerCase()}"
        val loader = Loader()
        if (streamName != null && topicName != null) {
            val messages = loader.getMessages(streamName, topicName)
            val disposableMessages = messages.subscribe(
                { result ->
                    val result = result
                },
                { error ->
                    Toast.makeText(context, "Ошибка ${error.message}", Toast.LENGTH_LONG).show()
                    Log.d("ZULIP", error.message.toString())
                }
            )
        }
    }

    private fun addMessage(newMessage: String): ViewTyped {
        val msg = Message(9, "You", newMessage, false, "14 Мар", null)
        return TextUi(msg.sender, msg.textMessage, null, R.layout.item_out_message, msg.textMessage)
    }
}


