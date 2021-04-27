package com.volboy.course_project.presentation.messages

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.setFragmentResultListener
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.volboy.course_project.App.Companion.messagesPresenter
import com.volboy.course_project.R
import com.volboy.course_project.databinding.FragmentMessagesBinding
import com.volboy.course_project.message_recycler_view.CommonAdapter
import com.volboy.course_project.message_recycler_view.CommonDiffUtilCallback
import com.volboy.course_project.message_recycler_view.MessageHolderFactory
import com.volboy.course_project.message_recycler_view.ViewTyped
import com.volboy.course_project.presentation.bottomfragment.EmojiBottomFragment
import com.volboy.course_project.presentation.mvp.presenter.MvpFragment
import com.volboy.course_project.presentation.streams.MvpSubscribedFragment
import java.util.*

class MvpMessagesFragment : MessagesView, MvpFragment<MessagesView, MessagesPresenter>(), MessageHolderFactory.MessageInterface {
    private lateinit var binding: FragmentMessagesBinding
    private lateinit var adapter: CommonAdapter<ViewTyped>
    private lateinit var topicName: String
    private lateinit var streamName: String
    private lateinit var lastMsgIdInTopic: String
    private var positionMsgOnLongClick = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentMessagesBinding.inflate(inflater, container, false)
        val holderFactory = MessageHolderFactory(this)
        adapter = CommonAdapter(holderFactory, CommonDiffUtilCallback())
        binding.recyclerMessage.adapter = adapter
        topicName = requireArguments().getString(MvpSubscribedFragment.ARG_TOPIC).toString()
        streamName = requireArguments().getString(MvpSubscribedFragment.ARG_STREAM).toString()
        lastMsgIdInTopic = requireArguments().getString(MvpSubscribedFragment.ARG_LAST_MSG_ID_IN_TOPIC).toString()
        (requireActivity() as AppCompatActivity).supportActionBar?.hide()
        getPresenter().loadFirstRemoteMessages(streamName, topicName)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.topicName.text = resources.getString(R.string.topic_name, topicName)
        binding.recyclerMessage.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                val linearLayoutManager = binding.recyclerMessage.layoutManager as LinearLayoutManager
                if (linearLayoutManager.findLastVisibleItemPosition() == adapter.items.size - 5) {
                    getPresenter().loadNextRemoteMessages(streamName, topicName, lastMsgIdInTopic)
                }
            }
        })
        setFragmentResultListener(EmojiBottomFragment.ARG_BOTTOM_FRAGMENT) { _, bundle ->
            val emojiList: ArrayList<String>? = bundle.getStringArrayList(EmojiBottomFragment.ARG_EMOJI)
            if (emojiList != null) {
                getPresenter().addOrDeleteReaction(positionMsgOnLongClick, emojiList)
            }
        }
    }

    override fun getPresenter(): MessagesPresenter = messagesPresenter

    override fun getMvpView(): MessagesView = this

    override fun showMessage(data: List<ViewTyped>) {
        binding.recyclerMessage.isVisible = true
        binding.messageBox.isVisible = true
        binding.messageBtn.isVisible = true
        binding.fragmentError.root.isGone = true
        binding.fragmentLoading.root.isGone = true
        adapter.items = data
    }

    override fun sendMessage(message: ViewTyped) {
        TODO("Not yet implemented")
    }

    override fun addReaction() {
        TODO("Not yet implemented")
    }

    override fun deleteReactions() {
        TODO("Not yet implemented")
    }

    override fun showLocalError() {
        TODO("Not yet implemented")
    }

    override fun showLocalLoading() {
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
        TODO("Not yet implemented")
    }

    override fun getLongClickedView(position: Int): Boolean {
        positionMsgOnLongClick = position + 1 //+1 потому, что реакции отдельным ViewType и находятся ниже сообщения
        val emojiBottomFragment = EmojiBottomFragment()
        emojiBottomFragment.show(parentFragmentManager, emojiBottomFragment.tag)
        return true
    }

    override fun getClickedView(view: View, position: Int) {
        TODO("Not yet implemented")
    }
}