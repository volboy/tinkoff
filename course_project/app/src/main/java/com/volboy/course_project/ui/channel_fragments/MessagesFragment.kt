package com.volboy.course_project.ui.channel_fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.volboy.course_project.App
import com.volboy.course_project.App.Companion.appDatabase
import com.volboy.course_project.R
import com.volboy.course_project.databinding.FragmentMessagesBinding
import com.volboy.course_project.message_recycler_view.*
import com.volboy.course_project.message_recycler_view.simple_items.ErrorItem
import com.volboy.course_project.message_recycler_view.simple_items.ProgressItem
import com.volboy.course_project.model.*
import com.volboy.course_project.ui.channel_fragments.tab_layout_fragments.SubscribedFragment
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class MessagesFragment : Fragment(), MessageHolderFactory.MessageInterface {
    private lateinit var binding: FragmentMessagesBinding
    private lateinit var commonAdapter: CommonAdapter<ViewTyped>
    private lateinit var reactionsOfMessage: MutableList<Reaction>
    private var positionMessage = 0
    private var topicName = ""
    private var streamName = ""
    private var ownId = 0
    var loader = Loader()
    var lastMessageIdInTopic = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setFragmentResultListener()
        getOwnId()
        topicName = requireArguments().getString(SubscribedFragment.ARG_TOPIC).toString()
        streamName = requireArguments().getString(SubscribedFragment.ARG_STREAM).toString()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val holderFactory = MessageHolderFactory(this)
        val mActionBar = (requireActivity() as AppCompatActivity).supportActionBar
        commonAdapter = CommonAdapter(holderFactory, CommonDiffUtilCallback())
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        commonAdapter.items = listOf(ProgressItem)
        binding.recyclerMessage.adapter = commonAdapter
        (binding.recyclerMessage.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
        binding.topicName.text = StringBuilder(resources.getString(R.string.topic_name) + topicName.toLowerCase(Locale.ROOT))
        downLoadMessage()
        setPagination()

    }

    //TODO("Сделать это один раз при загрузке приложения и записать в БД или SharedPreference")
    private fun getOwnId() {
        val ownUser = loader.getOwnUser()
        val disposableMessages = ownUser.subscribe(
            { result ->
                ownId = result.user_id
            },
            { error ->
                showSnackbar(resources.getString(R.string.msg_network_error) + error.message)
            }
        )
    }

    private fun downLoadMessage() {
        val loader = Loader()
        if (streamName.isNotEmpty() && topicName.isNotEmpty()) {
            val messages = loader.getMessages(streamName, topicName)
            val disposableMessages = messages.subscribe(
                { result ->
                    commonAdapter.items = result
                    showSnackbar(resources.getString(R.string.msg_network_ok))
                },
                { error ->
                    commonAdapter.items = listOf(ErrorItem)
                    showSnackbar(resources.getString(R.string.msg_network_error) + error.message)
                }
            )
        }
    }

    private fun sendMessage(str: String) {
        App.instance.zulipApi.sendMessage("stream", streamName, str, topicName).enqueue(object : Callback<SendMessageResponse> {
            override fun onResponse(call: Call<SendMessageResponse>, response: Response<SendMessageResponse>) {
                if (response.isSuccessful) {
                    showSnackbar(resources.getString(R.string.msg_network_send_msg))
                } else {
                    showSnackbar(resources.getString(R.string.msg_network_send_msg_error))
                }
            }

            override fun onFailure(call: Call<SendMessageResponse>, t: Throwable) {
                showSnackbar(resources.getString(R.string.msg_network_send_msg_error) + t.message)
            }
        })
        //TODO("Переделать эту копипасту")
        val messages = loader.getMessagesNext(lastMessageIdInTopic, streamName, topicName)
        val disposableMessages = messages.subscribe(
            { result ->
                commonAdapter.items = commonAdapter.items + result
                val id = mutableListOf<Int>()
                result.forEach { item ->
                    if (item.viewType == R.layout.item_in_message) {
                        id.add(item.uid.toInt())
                    }
                }
                val gson = Gson()
                //TODO ("Обновление флага что сообщения прочитаны, нужно с ним решить")
                updateMessageFlags(gson.toJson(id))
            },
            { error ->
                commonAdapter.items = listOf(ErrorItem)
                showSnackbar(resources.getString(R.string.pagination_str) + error.message)
            }
        )
    }

    private fun addEmojiToMessage(messageId: Int, emojiName: String, reactionType: String) {
        App.instance.zulipApi.addReaction(messageId, emojiName, reactionType).enqueue(object : Callback<AddReactionResponse> {
            override fun onResponse(call: Call<AddReactionResponse>, response: Response<AddReactionResponse>) {
                if (response.isSuccessful) {
                    showSnackbar(resources.getString(R.string.msg_network_send_emoji))
                } else {
                    showSnackbar(resources.getString(R.string.msg_network_send_emoji_error))
                }
            }

            override fun onFailure(call: Call<AddReactionResponse>, t: Throwable) {
                showSnackbar(resources.getString(R.string.msg_network_send_emoji_error) + t.message)
            }
        })
    }

    private fun removeEmojiFromMessage(messageId: Int, emojiName: String, reactionType: String) {
        App.instance.zulipApi.removeReaction(messageId, emojiName, reactionType).enqueue(object : Callback<AddReactionResponse> {
            override fun onResponse(call: Call<AddReactionResponse>, response: Response<AddReactionResponse>) {
                if (response.isSuccessful) {
                    showSnackbar(resources.getString(R.string.msg_network_delete_emoji))
                } else {
                    showSnackbar(resources.getString(R.string.msg_network_delete_emoji_error))
                }
            }

            override fun onFailure(call: Call<AddReactionResponse>, t: Throwable) {
                showSnackbar(resources.getString(R.string.msg_network_delete_emoji_error) + t.message)
            }
        })
    }

    private fun updateMessageFlags(messages: String) {
        App.instance.zulipApi.updateMessageFlag(messages, "add", "read").enqueue(object : Callback<UpdateMessageFlag> {
            override fun onResponse(call: Call<UpdateMessageFlag>, response: Response<UpdateMessageFlag>) {
                if (response.isSuccessful) {

                }
            }

            override fun onFailure(call: Call<UpdateMessageFlag>, t: Throwable) {
                showSnackbar(resources.getString(R.string.msg_network_flags_error) + t.message)
            }
        })
    }

    private fun setPagination() {
        val topicsDao = appDatabase.topicsDao()
        val disposable = topicsDao.getAllTopics()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { topics ->
                    val thisTopic = topics.firstOrNull { topic -> topic.name == topicName }
                    if (thisTopic != null) {
                        lastMessageIdInTopic = thisTopic.max_id
                    }

                },
                { error -> showSnackbar(resources.getString(R.string.msg_database_error) + error.message) },
                {
                    showSnackbar(resources.getString(R.string.msg_database_empty))
                })
        binding.recyclerMessage.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                val linearLayoutManager = binding.recyclerMessage.layoutManager as LinearLayoutManager
                if (linearLayoutManager.findLastVisibleItemPosition() == commonAdapter.items.size - 5) {
                    if (commonAdapter.items.firstOrNull { item -> item.uid == lastMessageIdInTopic.toString() } == null) {
                        var i = 0
                        while (commonAdapter.items[linearLayoutManager.findLastVisibleItemPosition() + i] !is TextUi) {
                            i++
                        }
                        val itemId = commonAdapter.items[linearLayoutManager.findLastVisibleItemPosition() + i].uid
                        showSnackbar(resources.getString(R.string.pagination_str))
                        val messages = loader.getMessagesNext(itemId.toInt(), streamName, topicName)
                        val disposableMessages = messages.subscribe(
                            { result ->
                                commonAdapter.items = commonAdapter.items + result
                                val id = mutableListOf<Int>()
                                result.forEach { item ->
                                    if (item.viewType == R.layout.item_in_message) {
                                        id.add(item.uid.toInt())
                                    }
                                }
                                val gson = Gson()
                                //TODO ("Обновление флага что сообщения прочитаны, нужно с ним решить")
                                updateMessageFlags(gson.toJson(id))
                            },
                            { error ->
                                showSnackbar(resources.getString(R.string.pagination_str) + error.message)
                            }
                        )
                    }
                }
            }
        })
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
    }

    private fun setFragmentResultListener() {
        setFragmentResultListener(EmojiBottomFragment.ARG_BOTTOM_FRAGMENT) { _, bundle ->
            val emojiList = bundle.getStringArrayList(EmojiBottomFragment.ARG_EMOJI)
            reactionsOfMessage = (commonAdapter.items[positionMessage] as ReactionsUi).reactions
            if (emojiList != null) {
                val isFindEmoji = reactionsOfMessage.firstOrNull { String(Character.toChars(it.emojiCode.toInt(16))) == emojiList[1] }
                if (isFindEmoji != null) {
                    val isFindUser = isFindEmoji.users.firstOrNull { it == ownId }
                    if (isFindUser != null) {
                        removeEmojiFromMessage((commonAdapter.items[positionMessage - 1] as TextUi).uid.toInt(), emojiList[0], "unicode_emoji")
                    } else {
                        addEmojiToMessage((commonAdapter.items[positionMessage - 1] as TextUi).uid.toInt(), emojiList[0], "unicode_emoji")
                    }
                } else {
                    addEmojiToMessage((commonAdapter.items[positionMessage - 1] as TextUi).uid.toInt(), emojiList[0], "unicode_emoji")
                }
            }
            //TODO("Переделать на загрузку только измененного сообщения")
            downLoadMessage()
        }
    }

    private fun showSnackbar(msg: String) {
        Snackbar.make(binding.root, msg, Snackbar.LENGTH_SHORT).show()
        Log.i(resources.getString(R.string.log_string), msg)
    }
}


