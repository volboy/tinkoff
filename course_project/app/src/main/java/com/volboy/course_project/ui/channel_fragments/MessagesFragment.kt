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
import com.volboy.course_project.model.AddReactionResponse
import com.volboy.course_project.model.Loader
import com.volboy.course_project.model.Reaction
import com.volboy.course_project.model.SendMessageResponse
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
    private var ownId = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setFragmentResultListener(EmojiBottomFragment.ARG_BOTTOM_FRAGMENT) { _, bundle ->
            val emojiList=bundle.getStringArrayList(EmojiBottomFragment.ARG_EMOJI)
            if (emojiList != null) {
                addEmoji(emojiList[1], emojiList[0])
            }
        }
        val loader = Loader()
        val ownUser = loader.getOwnUser()
        val disposableMessages = ownUser.subscribe(
            { result ->
                ownId = result.user_id
            },
            { error ->
                Toast.makeText(context, "Ошибка ${error.message}", Toast.LENGTH_LONG).show()
                Log.d("ZULIP", error.message.toString())
            }
        )
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
        App.instance.zulipApi.sendMessage("stream", streamName, str, topicName).enqueue(object : Callback<SendMessageResponse> {
            override fun onResponse(call: Call<SendMessageResponse>, response: Response<SendMessageResponse>) {
                if (response.isSuccessful) {
                    downLoadMessage()
                } else {
                    Toast.makeText(context, " $response.code()", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<SendMessageResponse>, t: Throwable) {
                Toast.makeText(context, t.message, Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun addEmojiToMessage(messageId: Int, emojiName: String, reactionType: String) {
        App.instance.zulipApi.addReaction(messageId, emojiName, reactionType).enqueue(object : Callback<AddReactionResponse> {
            override fun onResponse(call: Call<AddReactionResponse>, response: Response<AddReactionResponse>) {
                if (response.isSuccessful) {
                    Toast.makeText(context, " Успех $response.code()", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(context, " Ошибка $response.code()", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<AddReactionResponse>, t: Throwable) {
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
        addEmoji((view as EmojiView).emoji, "")
    }

    private fun addEmoji(emojiCode: String, emojiName: String) {
        addEmojiToMessage((commonAdapter.items[positionMessage - 1] as TextUi).uid.toInt(), emojiName, "unicode_emoji")
        var positionEmoji = -1
        reactionsOfMessage = (commonAdapter.items[positionMessage] as ReactionsUi).reactions as MutableList<Reaction>
        //если список реакций пуск добавляем сразу
        if (reactionsOfMessage.isNullOrEmpty()) {
            addEmojiToMessage((commonAdapter.items[positionMessage - 1] as TextUi).uid.toInt(), emojiName, "unicode_emoji")
        } else {
            //ищем эмоджи который хотим добавить в списке реакций
            reactionsOfMessage.forEach { reaction ->
                if (reaction.emojiCode == emojiCode) {
                    positionEmoji = reactionsOfMessage.indexOf(reaction)
                }
            }
            //если не нашли такого эмоджи, сразу добавляем
            if (positionEmoji == -1) {

                //если нашли такой
                //проверяем ставил ли пользователь такой эмоджи
            } else {

            }
        }
        updateMessagesReactions()
    }

    private fun updateMessagesReactions() {

    }
}


