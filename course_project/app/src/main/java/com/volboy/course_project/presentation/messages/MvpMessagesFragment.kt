package com.volboy.course_project.presentation.messages

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
import com.volboy.course_project.App.Companion.messagesPresenter
import com.volboy.course_project.R
import com.volboy.course_project.databinding.FragmentMessagesBinding
import com.volboy.course_project.presentation.bottomfragment.EmojiBottomFragment
import com.volboy.course_project.presentation.mvp.presenter.MvpFragment
import com.volboy.course_project.presentation.streams.MvpSubscribedFragment
import com.volboy.course_project.recyclerview.*
import java.util.*

class MvpMessagesFragment : MessagesView, MvpFragment<MessagesView, MessagesPresenter>(), MessageHolderFactory.MessageInterface {
    private lateinit var binding: FragmentMessagesBinding
    private lateinit var adapter: CommonAdapter<ViewTyped>
    private lateinit var topicName: String
    private lateinit var streamName: String
    private var positionMsgOnLongClick = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentMessagesBinding.inflate(inflater, container, false)
        val holderFactory = MessageHolderFactory(this)
        topicName = requireArguments().getString(MvpSubscribedFragment.ARG_TOPIC).toString()
        streamName = requireArguments().getString(MvpSubscribedFragment.ARG_STREAM).toString()
        adapter = CommonAdapter(
            holderFactory,
            CommonDiffUtilCallback(),
            PaginationAdapterHelper { _ -> getPresenter().loadNextRemoteMessages(streamName, topicName) })
        binding.recyclerMessage.adapter = adapter
        (requireActivity() as AppCompatActivity).supportActionBar?.hide()
        getPresenter().loadFirstRemoteMessages(streamName, topicName)
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

    override fun getPresenter(): MessagesPresenter = messagesPresenter

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

    override fun sendMessage(message: ViewTyped) {
        TODO("Not yet implemented")
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
        positionMsgOnLongClick = position + 1 //+1 потому, что реакции отдельным ViewType и находятся ниже сообщения
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
}