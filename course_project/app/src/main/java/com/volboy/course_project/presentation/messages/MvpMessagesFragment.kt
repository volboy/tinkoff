package com.volboy.course_project.presentation.messages

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isGone
import androidx.core.view.isVisible
import com.volboy.course_project.App.Companion.messagesPresenter
import com.volboy.course_project.databinding.FragmentMessagesBinding
import com.volboy.course_project.message_recycler_view.ViewTyped
import com.volboy.course_project.presentation.mvp.presenter.MvpFragment
import com.volboy.course_project.presentation.streams.MvpSubscribedFragment

class MvpMessagesFragment : MessagesView, MvpFragment<MessagesView, MessagesPresenter>() {
    private lateinit var binding: FragmentMessagesBinding
    private lateinit var topicName: String
    private lateinit var streamName: String

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentMessagesBinding.inflate(inflater, container, false)
        topicName = requireArguments().getString(MvpSubscribedFragment.ARG_TOPIC).toString()
        streamName = requireArguments().getString(MvpSubscribedFragment.ARG_STREAM).toString()
        val mActionBar = (requireActivity() as AppCompatActivity).supportActionBar
        mActionBar?.hide()
        getPresenter().getMessages(streamName, topicName)
        return binding.root
    }

    override fun getPresenter(): MessagesPresenter = messagesPresenter

    override fun getMvpView(): MessagesView = this

    override fun showMessage(data: List<ViewTyped>) {
        binding.recyclerMessage.isVisible = true
        binding.messageBox.isVisible = true
        binding.messageBtn.isVisible = true
        binding.fragmentError.root.isGone = true
        binding.fragmentLoading.root.isGone = true
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
}