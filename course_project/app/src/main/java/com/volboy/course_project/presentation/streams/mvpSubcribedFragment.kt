package com.volboy.course_project.presentation.streams

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.volboy.course_project.databinding.FragmentStreamsBinding
import com.volboy.course_project.message_recycler_view.CommonAdapter
import com.volboy.course_project.message_recycler_view.ViewTyped
import com.volboy.course_project.message_recycler_view.simple_items.ErrorItem
import com.volboy.course_project.message_recycler_view.simple_items.ProgressItem

class mvpSubcribedFragment : Fragment(), StreamsView {
    private lateinit var binding: FragmentStreamsBinding
    private lateinit var rwStreams: RecyclerView
    private lateinit var adapter: CommonAdapter<ViewTyped>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentStreamsBinding.inflate(inflater, container, false)
        initView()
        return binding.root
    }

    override fun showStreams(streams: List<ViewTyped>) {
        adapter.items = streams
    }

    override fun showTopics(topics: List<ViewTyped>) {
        //TODO Отобразить топики
    }

    override fun hideTopics(topics: List<ViewTyped>) {
        //TODO Скрыть топики
    }

    override fun showError() {
        adapter.items = listOf(ErrorItem)
    }

    override fun showLoading() {
        adapter.items = listOf(ProgressItem)
    }

    private fun initView() {
        rwStreams = binding.rwAllStreams
        rwStreams.adapter = adapter
    }
}