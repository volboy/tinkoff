package com.volboy.course_project.ui.channel_fragments.all_streams

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.volboy.course_project.R
import com.volboy.course_project.databinding.FragmentAllStreamsBinding
import com.volboy.course_project.message_recycler_view.MessageHolderFactory
import com.volboy.course_project.message_recycler_view.CommonAdapter
import com.volboy.course_project.message_recycler_view.TextUi
import com.volboy.course_project.message_recycler_view.ViewTyped
import java.io.Serializable

class AllStreamsFragment : Fragment(), AllStreamsHolderFactory.ChannelsInterface {
    private lateinit var binding: FragmentAllStreamsBinding
    private var typedList = mutableListOf<ViewTyped>()
    private var listStreams = mutableListOf<Serializable>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentAllStreamsBinding.inflate(inflater, container, false)
        listStreams = mutableListOf("#general", Pair("Testing", 1240), Pair("Bruh", 24), "#Development", "#Desing", "#PR")
        listStreams.forEach { item ->
            if (item !is Pair<*, *>) {
                typedList.add(TitleUi(item.toString(), null, R.drawable.ic_arrow_down, R.layout.collapse_item, item.toString()))
            }
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val clickListener: (View) -> Unit = { view ->
            val streamMessage = view.findViewById<ImageView>(R.id.streamImage)
            streamMessage.setImageResource(R.drawable.ic_arrow_up)
            listStreams.forEach { item ->
                if (item is Pair<*, *>) {
                    typedList.add(
                        TitleUi(
                            item.first.toString(), item.second.toString() + " mes", null,
                            R.layout.expand_item, item.first.toString()
                        )
                    )
                }
            }
        }
        val holderFactory = AllStreamsHolderFactory(this)
        val commonAdapter = CommonAdapter<ViewTyped>(holderFactory)
        commonAdapter.items = typedList
        binding.rwAllStreams.adapter = commonAdapter

    }

    override fun getClickedView(view: View, position: Int, viewType:Int) {
        TODO("Not yet implemented")
    }
}