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
import com.volboy.course_project.ui.channel_fragments.all_streams.AllStreamsHolderFactory
import com.volboy.course_project.ui.channel_fragments.all_streams.TitleUi
import java.io.Serializable

class SubscribedFragment : Fragment() {
    private var typedList = mutableListOf<ViewTyped>()
    private var listStreams = mutableListOf<Serializable>()
    private var selectedItem = false
    private lateinit var binding: FragmentSubscribedBinding
    private lateinit var clickListener: (View) -> Unit
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentSubscribedBinding.inflate(inflater, container, false)
        listStreams = mutableListOf("#general", Pair("Testing", 1240), "#Desing", "#PR")
        getStreams()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        clickListener = { view ->
            val streamMessage = view.findViewById<ImageView>(R.id.streamImage)
            view.isSelected = !view.isSelected
            selectedItem = view.isSelected
            if (selectedItem) {
                streamMessage.setImageResource(R.drawable.ic_arrow_up)
            } else {
                streamMessage.setImageResource(R.drawable.ic_arrow_down)
            }
            getStreams()
            binding.rwAllStreams.adapter?.notifyDataSetChanged()

        }
        val holderFactory = AllStreamsHolderFactory(clickListener)
        val commonAdapter = CommonAdapter<ViewTyped>(holderFactory)
        commonAdapter.items = typedList
        binding.rwAllStreams.adapter = commonAdapter
    }

    private fun getStreams() {
        typedList.clear()
        if (selectedItem) {
            listStreams.forEach { item ->
                if (item is Pair<*, *>) {
                    typedList.add(
                        TitleUi(
                            item.first.toString(), item.second.toString() + " mes", null,
                            R.layout.expand_item, item.first.toString()
                        )
                    )
                } else {
                    typedList.add(TitleUi(item.toString(), null, R.drawable.ic_arrow_down, R.layout.collapse_item, item.toString()))
                }
            }
        } else {
            listStreams.forEach { item ->
                if (item !is Pair<*, *>) {
                    typedList.add(TitleUi(item.toString(), null, R.drawable.ic_arrow_down, R.layout.collapse_item, item.toString()))
                }
            }
        }
    }
}