package com.volboy.courseproject.presentation.messages

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.setFragmentResultListener
import com.google.android.material.snackbar.Snackbar
import com.volboy.courseproject.App.Companion.component
import com.volboy.courseproject.R
import com.volboy.courseproject.databinding.FragmentMessagesBinding
import com.volboy.courseproject.presentation.bottomfragment.EmojiBottomFragment
import com.volboy.courseproject.presentation.bottomfragment.EmojiBottomFragment.Companion.ARG_MESSAGE
import com.volboy.courseproject.presentation.bottomfragment.EmojiBottomFragment.Companion.ARG_NEW_TOPIC
import com.volboy.courseproject.presentation.bottominfo.BottomInfoFragment
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
    private var messageId = 0

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
        (requireActivity() as AppCompatActivity).supportActionBar?.hide()
        (requireActivity() as AppCompatActivity).window.statusBarColor = ContextCompat.getColor(requireActivity(), R.color.header_color)
        binding.recyclerMessage.adapter = adapter
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.topicName.text = resources.getString(R.string.topic_name, topicName)
        binding.streamName.text = resources.getString(R.string.stream_name, streamName)
        getPresenter().loadFirstRemoteMessages(streamName, topicName, streamId)
        setFragmentResultListener(EmojiBottomFragment.ACTION_EMOJI) { _, bundle ->
            val emojiList: ArrayList<String>? = bundle.getStringArrayList(EmojiBottomFragment.ARG_EMOJI)
            if (emojiList != null) {
                getPresenter().addOrDeleteReaction(positionMsgOnLongClick, emojiList)
            }
        }
        setFragmentResultListener(EmojiBottomFragment.ACTION_DELETE) { _, _ ->
            getPresenter().deleteMessage(messageId, positionMsgOnLongClick + 1)
        }
        setFragmentResultListener(EmojiBottomFragment.ACTION_EDIT) { _, bundle ->
            getPresenter().editMessage(messageId, positionMsgOnLongClick + 1, bundle.getString(ARG_MESSAGE).toString())
        }
        setFragmentResultListener(EmojiBottomFragment.ACTION_CHANGE) { _, bundle ->
            getPresenter().changeTopicOfStream(messageId, positionMsgOnLongClick + 1, bundle.getString(ARG_NEW_TOPIC).toString())
        }
        setFragmentResultListener(EmojiBottomFragment.ACTION_COPY) { _, _ ->
            Snackbar.make(binding.root, getString(R.string.copy_done_str), Snackbar.LENGTH_SHORT).show()
        }
        binding.recyclerMessage.addOnLayoutChangeListener { _, _, _, _, bottom, _, _, _, oldBottom ->
            if (bottom < oldBottom) {
                binding.recyclerMessage.smoothScrollToPosition(0)
            }
        }
        binding.messageBtn.setOnClickListener {
            val str = binding.messageEdit.text.toString()
            if (str.isNotEmpty()) {
                getPresenter().sendMessage(str, streamName, topicName)
                binding.messageEdit.text.clear()
            }
            binding.messageBtn.setImageResource(R.drawable.ic_add_message)
        }
        binding.messageEdit.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (binding.messageEdit.text.isNotEmpty()) {
                    binding.messageBtn.setImageResource(R.drawable.ic_send_message)
                } else {
                    binding.messageBtn.setImageResource(R.drawable.ic_add_message)
                }
            }
        })
        binding.streamName.setOnClickListener { requireActivity().supportFragmentManager.popBackStack() }
    }

    override fun getPresenter(): MessagesPresenter = messagePresenter

    override fun getMvpView(): MessagesView = this

    override fun showMessage(data: List<ViewTyped>, position: Int) {
        show()
        adapter.items = data
    }

    override fun updateMessage(data: List<ViewTyped>, msgPosition: Int) {
        show()
        adapter.items = data
        adapter.notifyItemChanged(msgPosition)
    }

    override fun sendMessage(data: List<ViewTyped>, msgPosition: Int) {
        show()
        adapter.items = data
        adapter.notifyItemInserted(msgPosition)
        binding.recyclerMessage.smoothScrollToPosition(msgPosition)
    }

    override fun deleteMessage(data: List<ViewTyped>, msgPosition: Int) {
        adapter.items = data
        adapter.notifyItemRemoved(msgPosition)
        Snackbar.make(binding.root, getString(R.string.success_operation), Snackbar.LENGTH_SHORT).show()
    }

    override fun showInfo(title: String, msg: String) {
        val bottomInfoFragment = BottomInfoFragment()
        bottomInfoFragment.arguments = bundleOf(
            BottomInfoFragment.ARG_INFO_TITLE to title,
            BottomInfoFragment.ARG_INFO_TEXT to msg
        )
        bottomInfoFragment.show(parentFragmentManager, bottomInfoFragment.tag)
    }

    override fun showLoading(msg: String) {
        binding.recyclerMessage.isGone = true
        binding.messageBox.isGone = true
        binding.messageBtn.isGone = true
    }

    override fun showError(error: String?) {
        binding.recyclerMessage.isGone = true
        binding.messageBox.isGone = true
        binding.messageBtn.isGone = true
    }

    override fun getLongClickedView(position: Int): Boolean {
        messageId = adapter.items[position].uid.toInt()
        positionMsgOnLongClick = position - 1 //+1 ????????????, ?????? ?????????????? ?????????????????? ViewType ?? ?????????????????? ???????? ??????????????????
        val emojiBottomFragment = EmojiBottomFragment()
        emojiBottomFragment.arguments = bundleOf(
            ARG_MESSAGE to (adapter.items[position] as TextUi).message.toString(),
            ARG_TOPIC to topicName
        )
        emojiBottomFragment.show(parentFragmentManager, emojiBottomFragment.tag)
        return true
    }

    override fun getClickedView(viewCode: String, viewName: String, position: Int) {
        if (viewName.isEmpty()) {
            positionMsgOnLongClick = position
            val emojiBottomFragment = EmojiBottomFragment()
            emojiBottomFragment.show(parentFragmentManager, emojiBottomFragment.tag)
        } else {
            getPresenter().addOrDeleteReaction(position, arrayListOf(viewName, viewCode))
        }
    }

    private fun show() {
        binding.recyclerMessage.isVisible = true
        binding.messageBox.isVisible = true
        binding.messageBtn.isVisible = true
    }

    override fun onDetach() {
        super.onDetach()
        (requireActivity() as AppCompatActivity).supportActionBar?.show()
        (requireActivity() as AppCompatActivity).window.statusBarColor = ContextCompat.getColor(requireActivity(), R.color.component_color)
    }

    companion object {
        const val ARG_TOPIC = "topic"
        const val ARG_STREAM = "stream"
        const val ARG_STREAM_ID = "streamId"
        const val ARG_LAST_MSG_ID_IN_TOPIC = "lastId"
        const val FROM_TOPIC_TO_MESSAGE = "FromTopicToMessageFragment"
    }
}