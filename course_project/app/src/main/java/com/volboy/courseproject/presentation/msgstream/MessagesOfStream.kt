package com.volboy.courseproject.presentation.msgstream

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.core.view.isGone
import androidx.core.view.isVisible
import com.volboy.courseproject.App
import com.volboy.courseproject.R
import com.volboy.courseproject.databinding.FragmentMsgStreamBinding
import com.volboy.courseproject.presentation.bottominfo.BottomInfoFragment
import com.volboy.courseproject.presentation.mvp.presenter.MvpFragment
import com.volboy.courseproject.recyclerview.*
import javax.inject.Inject

class MessagesOfStream : MessagesOfStreamView, MvpFragment<MessagesOfStreamView, MessagesOfStreamsPresenter>(), MessageHolderFactory.MessageInterface {

    private lateinit var binding: FragmentMsgStreamBinding
    private lateinit var adapter: CommonAdapter<ViewTyped>
    private lateinit var streamName: String
    private var streamId = 0

    @Inject
    lateinit var messageOfStreamsPresenter: MessagesOfStreamsPresenter

    init {
        App.component.injectAllMessagesPresenter(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentMsgStreamBinding.inflate(inflater, container, false)
        val holderFactory = MessageHolderFactory(this)
        streamName = requireArguments().getString(ARG_STREAM).toString()
        streamId = requireArguments().getInt(ARG_STREAM_ID)
        adapter = CommonAdapter(
            holderFactory,
            CommonDiffUtilCallback(),
            PaginationAdapterHelper { getPresenter().loadMessageOfStreamNext(streamName) })
        binding.recyclerMessage.adapter = adapter
        (requireActivity() as AppCompatActivity).supportActionBar?.hide()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getPresenter().loadMessageOfStream(streamName, streamId)
        binding.recyclerMessage.addOnLayoutChangeListener { _, _, _, _, bottom, _, _, _, oldBottom ->
            if (bottom < oldBottom) {
                binding.recyclerMessage.smoothScrollToPosition(0)
            }
        }
        binding.messageBtn.setOnClickListener {
            val str = binding.messageBox.text.toString()
            if (str.isNotEmpty()) {
                getPresenter().sendMessage(str, streamName, binding.topicBox.text.toString())
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
                    binding.topicBox.isVisible = true
                } else {
                    binding.messageBtn.setImageResource(R.drawable.ic_add_message)
                    binding.topicBox.isGone = true
                }
            }
        })
    }

    override fun getPresenter(): MessagesOfStreamsPresenter = messageOfStreamsPresenter

    override fun getMvpView(): MessagesOfStreamView = this

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
        binding.recyclerMessage.smoothScrollToPosition(msgPosition)
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
        binding.fragmentLoading.root.isVisible = true
        binding.recyclerMessage.isGone = true
        binding.messageBox.isGone = true
        binding.messageBtn.isGone = true
        binding.topicBox.isGone = true
        binding.fragmentError.root.isGone = true
    }

    override fun showError(error: String?) {
        binding.fragmentError.root.isVisible = true
        binding.fragmentLoading.root.isGone = true
        binding.recyclerMessage.isGone = true
        binding.messageBox.isGone = true
        binding.topicBox.isGone = true
        binding.messageBtn.isGone = true
    }

    override fun getLongClickedView(position: Int): Boolean {
        Log.i(getString(R.string.log_string), "Лонг тап")
        return true
    }

    override fun getClickedView(viewCode: String, viewName: String, position: Int) {
        Log.i(getString(R.string.log_string), viewName)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        (requireActivity() as AppCompatActivity).supportActionBar?.show()
    }

    private fun show() {
        binding.recyclerMessage.isVisible = true
        binding.messageBox.isVisible = true
        binding.messageBtn.isVisible = true
        binding.fragmentError.root.isGone = true
        binding.fragmentLoading.root.isGone = true
    }

    companion object {
        const val ARG_STREAM = "stream"
        const val ARG_STREAM_ID = "streamId"
    }
}