package com.volboy.courseproject.presentation.messages

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.setFragmentResultListener
import com.volboy.courseproject.App.Companion.component
import com.volboy.courseproject.R
import com.volboy.courseproject.databinding.FragmentMessagesBinding
import com.volboy.courseproject.presentation.bottomfragment.EmojiBottomFragment
import com.volboy.courseproject.presentation.mvp.presenter.MvpFragment
import com.volboy.courseproject.recyclerview.*
import java.util.*
import javax.inject.Inject

class MvpMessagesFragment : MessagesView, MvpFragment<MessagesView, MessagesPresenter>(), MessageHolderFactory.MessageInterface {
    private lateinit var binding: FragmentMessagesBinding
    private lateinit var adapter: CommonAdapter<ViewTyped>
    private lateinit var topicName: String
    private lateinit var streamName: String
    private var streamId = 0
    private var positionMsgOnLongClick = 0

    @Inject
    lateinit var messagePresenter: MessagesPresenter

    init {
        component.injectMessagePresenter(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentMessagesBinding.inflate(inflater, container, false)
        val holderFactory = MessageHolderFactory(this)
        topicName = requireArguments().getString(ARG_TOPIC).toString()
        streamName = requireArguments().getString(ARG_STREAM).toString()
        streamId = requireArguments().getInt(ARG_STREAM_ID)
        adapter = CommonAdapter(
            holderFactory,
            CommonDiffUtilCallback(),
            PaginationAdapterHelper { getPresenter().loadNextRemoteMessages(streamName, topicName) })
        binding.recyclerMessage.adapter = adapter
        (requireActivity() as AppCompatActivity).supportActionBar?.hide()
        getPresenter().loadFirstRemoteMessages(streamName, topicName, streamId)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.topicName.text = resources.getString(R.string.topic_name, topicName)
        setFragmentResultListener(EmojiBottomFragment.ARG_BOTTOM_FRAGMENT) { _, bundle ->
            val emojiList: ArrayList<String>? = bundle.getStringArrayList(EmojiBottomFragment.ARG_EMOJI)
            if (emojiList != null) {
                getPresenter().addOrDeleteReaction(positionMsgOnLongClick, emojiList)
            }
        }
        binding.recyclerMessage.addOnLayoutChangeListener { _, _, _, _, bottom, _, _, _, oldBottom ->
            if (bottom < oldBottom) {
                binding.recyclerMessage.smoothScrollToPosition(0)
            }
        }
        binding.messageBtn.setOnClickListener {
            val str = binding.messageBox.text.toString()
            if (str.isNotEmpty()) {
                getPresenter().sendMessage(str, streamName, topicName)
                binding.messageBox.text.clear()
            }
            binding.messageBtn.setImageResource(R.drawable.ic_add_message)
        }
        binding.messageBox.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (binding.messageBox.text.isNotEmpty()) {
                    binding.messageBtn.setImageResource(R.drawable.ic_send_message)
                } else {
                    binding.messageBtn.setImageResource(R.drawable.ic_add_message)
                }
            }
        })
    }

    override fun getPresenter(): MessagesPresenter = messagePresenter

    override fun getMvpView(): MessagesView = this

    override fun showMessage(data: List<ViewTyped>, position: Int) {
        show()
        adapter.items = data
        binding.recyclerMessage.smoothScrollToPosition(position)
    }

    override fun updateMessage(data: List<ViewTyped>, msgPosition: Int) {
        show()
        adapter.items = data
        adapter.notifyItemChanged(msgPosition)
    }

    override fun sendMessage(data: List<ViewTyped>, msgPosition: Int) {
        show()
        adapter.items = data
        binding.recyclerMessage.smoothScrollToPosition(msgPosition)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        (requireActivity() as AppCompatActivity).supportActionBar?.show()
    }

    override fun showLoading(msg: String) {
        binding.fragmentLoading.root.isVisible = true
        binding.recyclerMessage.isGone = true
        binding.messageBox.isGone = true
        binding.messageBtn.isGone = true
        binding.fragmentError.root.isGone = true
    }

    override fun showError(error: String?) {
        binding.fragmentError.root.isVisible = true
        binding.fragmentLoading.root.isGone = true
        binding.recyclerMessage.isGone = true
        binding.messageBox.isGone = true
        binding.messageBtn.isGone = true
    }

    override fun getLongClickedView(position: Int): Boolean {
        positionMsgOnLongClick = position - 1 //+1 потому, что реакции отдельным ViewType и находятся ниже сообщения
        val emojiBottomFragment = EmojiBottomFragment()
        emojiBottomFragment.show(parentFragmentManager, emojiBottomFragment.tag)
        return true
    }

    override fun getClickedEmoji(emojiCode: String, emojiName: String, position: Int) {
        if (emojiName.isEmpty()) {
            positionMsgOnLongClick = position
            val emojiBottomFragment = EmojiBottomFragment()
            emojiBottomFragment.show(parentFragmentManager, emojiBottomFragment.tag)
        } else {
            getPresenter().addOrDeleteReaction(position, arrayListOf(emojiName, emojiCode))
        }
    }

    private fun show() {
        binding.recyclerMessage.isVisible = true
        binding.messageBox.isVisible = true
        binding.messageBtn.isVisible = true
        binding.fragmentError.root.isGone = true
        binding.fragmentLoading.root.isGone = true
    }

    companion object {
        const val ARG_TOPIC = "topic"
        const val ARG_STREAM = "stream"
        const val ARG_STREAM_ID = "streamId"
        const val ARG_LAST_MSG_ID_IN_TOPIC = "lastId"
        const val FROM_TOPIC_TO_MESSAGE = "FromTopicToMessageFragment"
    }
}