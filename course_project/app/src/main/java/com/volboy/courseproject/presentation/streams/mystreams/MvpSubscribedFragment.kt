package com.volboy.courseproject.presentation.streams.mystreams

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.appcompat.widget.SwitchCompat
import androidx.core.os.bundleOf
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.volboy.courseproject.App.Companion.component
import com.volboy.courseproject.R
import com.volboy.courseproject.databinding.FragmentSubscribedBinding
import com.volboy.courseproject.presentation.bottominfo.BottomInfoFragment
import com.volboy.courseproject.presentation.bottominfo.BottomInfoFragment.Companion.ARG_INFO_TEXT
import com.volboy.courseproject.presentation.bottominfo.BottomInfoFragment.Companion.ARG_INFO_TITLE
import com.volboy.courseproject.presentation.messages.MvpMessagesFragment
import com.volboy.courseproject.presentation.messages.MvpMessagesFragment.Companion.ARG_LAST_MSG_ID_IN_TOPIC
import com.volboy.courseproject.presentation.messages.MvpMessagesFragment.Companion.ARG_STREAM
import com.volboy.courseproject.presentation.messages.MvpMessagesFragment.Companion.ARG_STREAM_ID
import com.volboy.courseproject.presentation.messages.MvpMessagesFragment.Companion.ARG_TOPIC
import com.volboy.courseproject.presentation.messages.MvpMessagesFragment.Companion.FROM_TOPIC_TO_MESSAGE
import com.volboy.courseproject.presentation.mvp.presenter.MvpFragment
import com.volboy.courseproject.presentation.streams.UiHolderFactory
import com.volboy.courseproject.recyclerview.CommonAdapter
import com.volboy.courseproject.recyclerview.CommonDiffUtilCallback
import com.volboy.courseproject.recyclerview.ViewTyped
import javax.inject.Inject

class MvpSubscribedFragment : SubStreamsView, MvpFragment<SubStreamsView, SubStreamsPresenter>(),
    UiHolderFactory.ChannelsInterface {
    private lateinit var binding: FragmentSubscribedBinding
    private lateinit var adapter: CommonAdapter<ViewTyped>
    private lateinit var clickedStream: TitleUi

    @Inject
    lateinit var subStreamsPresenter: SubStreamsPresenter

    init {
        component.injectStreamsPresenter(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentSubscribedBinding.inflate(inflater, container, false)
        val holderFactory = UiHolderFactory(this)
        val linearLayoutManager: LinearLayoutManager = object : LinearLayoutManager(requireContext()) {
            override fun scrollVerticallyBy(dx: Int, recycler: RecyclerView.Recycler, state: RecyclerView.State): Int {
                val scrollRange = super.scrollVerticallyBy(dx, recycler, state)
                val overScroll = dx - scrollRange
                if (overScroll < -10) {
                    getPresenter().getStreams()
                }
                return scrollRange
            }
        }
        binding.rwAllStreams.layoutManager = linearLayoutManager
        adapter = CommonAdapter(holderFactory, CommonDiffUtilCallback(), null)
        binding.rwAllStreams.adapter = adapter
        binding.rwAllStreams.addItemDecoration(StreamsItemDecoration(requireContext()))
        val searchEdit = requireActivity().findViewById<EditText>(R.id.searchEditText)
        getPresenter().getStreams()
        getPresenter().setSearchObservable(searchEdit)
        return binding.root
    }

    override fun getPresenter(): SubStreamsPresenter = subStreamsPresenter

    override fun getMvpView(): SubStreamsView = this

    override fun showData(data: List<ViewTyped>) {
        binding.rwAllStreams.isVisible = true
        binding.fragmentError.root.isGone = true
        binding.fragmentLoading.root.isGone = true
        adapter.items = data
    }

    override fun updateData(data: List<ViewTyped>, position: Int) {
        binding.rwAllStreams.isVisible = true
        binding.fragmentError.root.isGone = true
        binding.fragmentLoading.root.isGone = true
        adapter.items = data
        adapter.notifyItemInserted(position)
    }

    override fun hideData(data: List<ViewTyped>) {
        adapter.items = data
    }

    override fun showMessage(title: String, msg: String) {
        val bottomInfoFragment = BottomInfoFragment()
        bottomInfoFragment.arguments = bundleOf(
            ARG_INFO_TITLE to title,
            ARG_INFO_TEXT to msg
        )
        bottomInfoFragment.show(parentFragmentManager, bottomInfoFragment.tag)
    }

    override fun showError(error: String?) {
        binding.rwAllStreams.isGone = true
        binding.fragmentError.root.isVisible = true
        binding.fragmentLoading.root.isGone = true
        binding.fragmentError.errorText.text = error
        binding.fragmentError.retryText.setOnClickListener { getPresenter().getStreams() }
    }

    override fun showLoading(msg: String) {
        binding.rwAllStreams.isGone = true
        binding.fragmentError.root.isGone = true
        binding.fragmentLoading.root.isVisible = true
    }

    override fun getClickedView(view: View, position: Int, viewType: Int) {
        when (viewType) {
            R.layout.item_collapse -> {
                clickedStream = adapter.items[position] as TitleUi
                clickedStream.isSelected = !clickedStream.isSelected
                if (clickedStream.isSelected) {
                    getPresenter().getTopics(position)
                } else {
                    getPresenter().removeTopics(position)
                }
            }
            R.layout.item_expand -> {
                val messagesFragment = MvpMessagesFragment()
                messagesFragment.arguments = bundleOf(
                    ARG_TOPIC to (adapter.items[position] as TitleUi).title,
                    ARG_LAST_MSG_ID_IN_TOPIC to (adapter.items[position] as TitleUi).uid,
                    ARG_STREAM to clickedStream.title,
                    ARG_STREAM_ID to clickedStream.uid
                )
                requireActivity().supportFragmentManager.beginTransaction()
                    .addToBackStack(FROM_TOPIC_TO_MESSAGE)
                    .add(R.id.container, messagesFragment)
                    .commit()
            }
        }
    }

    override fun getClickedSwitch(view: SwitchCompat, streamName: String) {
        TODO("Not yet implemented")
    }

}