package com.volboy.course_project.presentation.streams

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.volboy.course_project.App.Companion.streamsPresenter
import com.volboy.course_project.R
import com.volboy.course_project.databinding.FragmentStreamsBinding
import com.volboy.course_project.message_recycler_view.CommonAdapter
import com.volboy.course_project.message_recycler_view.CommonDiffUtilCallback
import com.volboy.course_project.message_recycler_view.ViewTyped
import com.volboy.course_project.message_recycler_view.simple_items.ErrorItem
import com.volboy.course_project.message_recycler_view.simple_items.ProgressItem
import com.volboy.course_project.presentation.mvp.presenter.MvpFragment
import com.volboy.course_project.ui.channel_fragments.MessagesFragment
import com.volboy.course_project.ui.channel_fragments.tab_layout_fragments.TitleUi
import com.volboy.course_project.ui.channel_fragments.tab_layout_fragments.UiHolderFactory

class MvpSubscribedFragment : StreamsView, MvpFragment<StreamsView, StreamsPresenter>(), UiHolderFactory.ChannelsInterface {
    private lateinit var binding: FragmentStreamsBinding
    private lateinit var rwStreams: RecyclerView
    private lateinit var adapter: CommonAdapter<ViewTyped>
    private lateinit var clickedStream: TitleUi

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentStreamsBinding.inflate(inflater, container, false)
        val holderFactory = UiHolderFactory(this)
        adapter = CommonAdapter(holderFactory, CommonDiffUtilCallback())
        rwStreams = binding.rwAllStreams
        rwStreams.adapter = adapter
        getPresenter().getStreams()
        return binding.root
    }

    override fun getPresenter(): StreamsPresenter = streamsPresenter

    override fun getMvpView(): StreamsView = this

    override fun showData(data: List<ViewTyped>) {
        adapter.items = data
    }

    override fun hideData(data: List<ViewTyped>) {
        adapter.items = data
    }

    override fun showError(error: String?) {
        //TODO("Сделать обработку ошибки и отображать ее")
        adapter.items = listOf(ErrorItem)
    }

    override fun showLoading(msg: String) {
        adapter.items = listOf(ProgressItem)
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
                val messagesFragment = MessagesFragment()
                val arguments = Bundle()
                arguments.putString(ARG_TOPIC, (adapter.items[position] as TitleUi).title)
                arguments.putString(ARG_STREAM, clickedStream.title)
                messagesFragment.arguments = arguments
                val transaction = requireActivity().supportFragmentManager.beginTransaction()
                transaction.addToBackStack(FROM_TOPIC_TO_MESSAGE)
                transaction.add(R.id.container, messagesFragment)
                transaction.commit()
            }
        }
    }

    companion object {
        const val ARG_TOPIC = "topic"
        const val ARG_STREAM = "stream"
        const val FROM_TOPIC_TO_MESSAGE = "FromSubscribedFragmentToMessageFragment"
    }
}