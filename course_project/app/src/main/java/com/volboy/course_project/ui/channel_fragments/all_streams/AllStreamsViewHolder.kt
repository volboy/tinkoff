package com.volboy.course_project.ui.channel_fragments.all_streams

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.volboy.course_project.R
import com.volboy.course_project.message_recycler_view.BaseViewHolder
import com.volboy.course_project.message_recycler_view.ViewTyped

class TitleUi(
    val title: String,
    val count: String?,
    val imageId: Int?,
    override val viewType: Int = R.layout.collapse_item,
    override val uid: String = ""
) : ViewTyped

class AllStreamsViewHolder(val view: View, private val channelsInterface: AllStreamsHolderFactory.ChannelsInterface) :
    BaseViewHolder<TitleUi>(view) {
    private val title: TextView = view.findViewById(R.id.streamText)
    private val count: TextView = view.findViewById(R.id.messagesCount)
    private val image: ImageView = view.findViewById(R.id.streamImage)

    override fun bind(item: TitleUi) {
        view.setOnClickListener {
            channelsInterface.getClickedView(view, layoutPosition)
        }
        title.text = item.title
        count.text = item.count.toString()
        item.imageId?.let {
            image.setImageResource(it)
        }
    }
}

