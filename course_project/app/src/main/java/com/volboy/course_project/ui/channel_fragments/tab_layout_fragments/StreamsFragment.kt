package com.volboy.course_project.ui.channel_fragments.tab_layout_fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.volboy.course_project.R
import com.volboy.course_project.databinding.FragmentStreamsBinding
import com.volboy.course_project.databinding.FragmentSubscribedBinding
import com.volboy.course_project.message_recycler_view.CommonAdapter
import com.volboy.course_project.message_recycler_view.ViewTyped
import com.volboy.course_project.model.ObservableStreams
import com.volboy.course_project.ui.channel_fragments.MessagesFragment
import java.io.Serializable

class StreamsFragment : Fragment(), UiHolderFactory.ChannelsInterface {
    private lateinit var binding: FragmentStreamsBinding
    private val loaderStreams = ObservableStreams()
    private var listStreams = listOf<ViewTyped>()
    private lateinit var commonAdapter: CommonAdapter<ViewTyped>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentStreamsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val holderFactory = UiHolderFactory(this)
        commonAdapter = CommonAdapter(holderFactory)
        val observableStreams = loaderStreams.getStreams().subscribe(
            { item ->
                listStreams = item
                commonAdapter.items = listStreams
            },
            { error -> Snackbar.make(binding.root, error.toString(), Snackbar.LENGTH_LONG).show() }
        )
        commonAdapter.items = listStreams
        binding.rwAllStreams.adapter = commonAdapter
    }

    override fun getClickedView(view: View, position: Int, viewType: Int) {
        val items: MutableList<ViewTyped> = commonAdapter.items.toMutableList()
        val item = items[position] as TitleUi
        val topics = item.topics
        view.isSelected = !view.isSelected
        when (viewType) {
            R.layout.item_collapse -> {
                if (view.isSelected) {
                    item.imageId = R.drawable.ic_arrow_up
                    item.uid = "UP"
                    topics?.forEach { topic ->
                        items.add(position + 1, TitleUi(topic.first, topic.second.toString() + " mes", null, 0, R.layout.item_expand, topic.first))
                    }
                } else {
                    item.imageId = R.drawable.ic_arrow_down
                    item.uid = "DOWN"
                    var topicSize = item.topics?.size
                    topics?.forEach { _ ->
                        topicSize = topicSize?.minus(1)
                        items.removeAt(position + topicSize!! + 1)
                    }
                }
                commonAdapter.items = items
                commonAdapter.notifyDataSetChanged()
            }
        }
    }
}
