package com.volboy.courseproject.presentation.msgstream

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.volboy.courseproject.App
import com.volboy.courseproject.databinding.FragmentMsgStreamBinding
import com.volboy.courseproject.presentation.mvp.presenter.MvpFragment
import com.volboy.courseproject.recyclerview.MessageHolderFactory
import com.volboy.courseproject.recyclerview.ViewTyped
import javax.inject.Inject

class AllMessagesOfStream : AllMessagesView, MvpFragment<AllMessagesView, AllMessagesPresenter>(), MessageHolderFactory.MessageInterface {
    private lateinit var binding: FragmentMsgStreamBinding

    @Inject
    lateinit var allMessagePresenter: AllMessagesPresenter

    init {
        App.component.injectAllMessagesPresenter(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentMsgStreamBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun showData(data: List<ViewTyped>, position: Int) {
        TODO("Not yet implemented")
    }

    override fun showLoading(msg: String) {
        TODO("Not yet implemented")
    }

    override fun showError(error: String?) {
        TODO("Not yet implemented")
    }

    override fun getPresenter(): AllMessagesPresenter = allMessagePresenter

    override fun getMvpView(): AllMessagesView = this

    override fun getLongClickedView(position: Int): Boolean {
        TODO("Not yet implemented")
    }

    override fun getClickedEmoji(emojiCode: String, emojiName: String, position: Int) {
        TODO("Not yet implemented")
    }
}