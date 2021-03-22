package com.volboy.course_project.ui.channel_fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.volboy.course_project.R
import com.volboy.course_project.databinding.FragmentSubscribedBinding
import com.volboy.course_project.message_recycler_view.CommonAdapter
import com.volboy.course_project.message_recycler_view.ViewTyped
import com.volboy.course_project.model.LoaderStreams
import com.volboy.course_project.ui.channel_fragments.all_streams.AllStreamsHolderFactory
import com.volboy.course_project.ui.channel_fragments.all_streams.TitleUi
import java.io.Serializable

class SubscribedFragment : Fragment(), AllStreamsHolderFactory.ChannelsInterface {
    private var loaderStreams = LoaderStreams()
    private lateinit var binding: FragmentSubscribedBinding
    private lateinit var commonAdapter: CommonAdapter<ViewTyped>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentSubscribedBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val listStreams = loaderStreams.convertStreams(0, false)
        val holderFactory = AllStreamsHolderFactory(this)
        commonAdapter = CommonAdapter<ViewTyped>(holderFactory)
        binding.rwAllStreams.adapter = commonAdapter
        commonAdapter.items = listStreams

    }

    override fun getClickedView(view: View, position: Int) {
        view.isSelected = !view.isSelected
        commonAdapter.items = loaderStreams.convertStreams(position, view.isSelected)
        binding.rwAllStreams.adapter = commonAdapter
        val streamMessage = view.findViewById<ImageView>(R.id.streamImage)
        if (view.isSelected) {
            streamMessage.setImageResource(R.drawable.ic_arrow_up)
        } else {
            streamMessage.setImageResource(R.drawable.ic_arrow_down)
        }
    }
}

