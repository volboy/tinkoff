package com.volboy.courseproject.presentation.streams.mystreams

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.volboy.courseproject.R
import com.volboy.courseproject.presentation.streams.UiHolderFactory
import com.volboy.courseproject.recyclerview.BaseViewHolder
import com.volboy.courseproject.recyclerview.ViewTyped

class TitleUi(
    val title: String,
    var count: Int = 0,
    var isSelected: Boolean,
    var streamsId: Int?,
    var imageId: Int,
    override val viewType: Int,
    override var uid: String
) : ViewTyped

class StreamsViewHolder(val view: View, private val channelsInterface: UiHolderFactory.ChannelsInterface) :
    BaseViewHolder<TitleUi>(view) {
    private val title: TextView = view.findViewById(R.id.streamText)
    private val count: TextView = view.findViewById(R.id.messagesCount)
    private val image: ImageView = view.findViewById(R.id.streamImage)

    override fun bind(item: TitleUi) {
        view.setOnClickListener {
            channelsInterface.getClickedView(view, adapterPosition, item.viewType)
        }
        title.text = item.title
        count.text = item.count.toString()
        image.setImageResource(item.imageId)
    }
}

