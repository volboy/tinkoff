package com.volboy.course_project.ui.channel_fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import com.volboy.course_project.App
import com.volboy.course_project.R
import com.volboy.course_project.customviews.EmojiView

import com.volboy.course_project.databinding.FragmentMessagesBinding
import com.volboy.course_project.message_recycler_view.*
import com.volboy.course_project.message_recycler_view.simple_items.ErrorItem
import com.volboy.course_project.message_recycler_view.simple_items.ProgressItem
import com.volboy.course_project.model.Loader
import com.volboy.course_project.model.Reaction
import com.volboy.course_project.model.ReactionsJSON
import com.volboy.course_project.model.SendedMessage
import com.volboy.course_project.ui.channel_fragments.tab_layout_fragments.SubscribedFragment
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MessagesFragment : Fragment(), MessageHolderFactory.MessageInterface {
    private lateinit var reactionsOfMessage: MutableList<Reaction>
    private lateinit var binding: FragmentMessagesBinding
    private lateinit var commonAdapter: CommonAdapter<ViewTyped>
    private var positionMessage = 0
    private var topicName = ""
    private var streamName = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setFragmentResultListener(EmojiBottomFragment.ARG_BOTTOM_FRAGMENT) { _, bundle ->
            bundle.getString(EmojiBottomFragment.ARG_EMOJI_CODE)?.let { addEmoji(it) }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val holderFactory = MessageHolderFactory(this)
        val mActionBar = (requireActivity() as AppCompatActivity).supportActionBar
        commonAdapter = CommonAdapter(holderFactory)
        binding = FragmentMessagesBinding.inflate(inflater, container, false)
        binding.messageBtn.setOnClickListener {
            val str = binding.messageBox.text.toString()
            if (str.isNotEmpty()) {
                sendMessage(str)
                binding.messageBox.text.clear()
            }
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
        mActionBar?.hide()
        return binding.root
    }

    private fun sendMessage(str: String) {
        App.instance.zulipApi.sendMessage("stream", streamName, str, topicName).enqueue(object : Callback<SendedMessage> {
            override fun onResponse(call: Call<SendedMessage>, response: Response<SendedMessage>) {
                if (response.isSuccessful) {
                    downLoadMessage()
                } else {
                    Toast.makeText(context, " $response.code()", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<SendedMessage>, t: Throwable) {
                Toast.makeText(context, t.message, Toast.LENGTH_LONG).show()
            }
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        commonAdapter.items = listOf(ProgressItem)
        binding.recyclerMessage.adapter = commonAdapter
        topicName = requireArguments().getString(SubscribedFragment.ARG_TOPIC).toString()
        streamName = requireArguments().getString(SubscribedFragment.ARG_STREAM).toString()
        binding.topicName.text = "Topic: #${topicName?.toLowerCase()}"
        downLoadMessage()
    }

    private fun downLoadMessage() {
        val loader = Loader()
        if (streamName.isNotEmpty() && topicName.isNotEmpty()) {
            val messages = loader.getMessages(streamName, topicName)
            val disposableMessages = messages.subscribe(
                { result ->
                    commonAdapter.items = result
                },
                { error ->
                    commonAdapter.items = listOf(ErrorItem)
                    Toast.makeText(context, "Ошибка ${error.message}", Toast.LENGTH_LONG).show()
                    Log.d("ZULIP", error.message.toString())
                }
            )
        }
    }

    //получаем сообщение на котором был LongClick
    override fun getLongClickedView(position: Int): Boolean {
        positionMessage = position + 1 //+1 потому что реакции отдельным viewType под текстом сообщения
        val emojiBottomFragment = EmojiBottomFragment()
        emojiBottomFragment.show(parentFragmentManager, emojiBottomFragment.tag)
        return true
    }

    //получаем emoji на котором был Click
    override fun getClickedView(view: View, position: Int) {
        view.isSelected = !view.isSelected
        positionMessage = position
        addEmoji((view as EmojiView).emoji)
    }

    private fun addEmoji(emoji: String) {
        var positionEmoji = -1
        reactionsOfMessage = (commonAdapter.items[positionMessage] as ReactionsUi).reactions as MutableList<Reaction>
        //если список реакций пуск добавляем сразу
        if (reactionsOfMessage.isNullOrEmpty()) {
            reactionsOfMessage.add(Reaction(emoji, 1, "some_type", mutableListOf(0)))
        } else {
            //ищем эмоджи который хотим добавить в списке реакций
            reactionsOfMessage.forEach { reaction ->
                if (reaction.emojiCode == emoji) {
                    positionEmoji = reactionsOfMessage.indexOf(reaction)
                }
            }
            //если не нашли такого эмоджи, сразу добавляем
            if (positionEmoji == -1) {
                //reactionsOfMessage.add(ReactionsJSON(emoji, "some_name", "some_type", 1))
                //если нашли такой
            } else {
                //проверяем ставил ли пользователь такой эмоджи
                /*  if ("You" in reactionsOfMessage[positionEmoji].users) {
                      reactionsOfMessage[positionEmoji].users.remove("You")
                      if (reactionsOfMessage[positionEmoji].users.size == 0) {
                          reactionsOfMessage.removeAt(positionEmoji)
                      }
                  } else {
                      reactionsOfMessage[positionEmoji].users.add("You")
                  }*/
            }
        }
        updateMessagesReactions()
    }

    private fun updateMessagesReactions() {
        (commonAdapter.items[positionMessage] as ReactionsUi).reactions = reactionsOfMessage
        commonAdapter.notifyItemChanged(positionMessage)
    }
}


