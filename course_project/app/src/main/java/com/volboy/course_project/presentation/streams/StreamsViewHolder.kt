package com.volboy.course_project.presentation.streams

import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import com.volboy.course_project.R
import com.volboy.course_project.message_recycler_view.BaseViewHolder
import com.volboy.course_project.message_recycler_view.ViewTyped
import com.volboy.course_project.model.TopicJSON

class TitleUi(
    val title: String,
    var count: Int = 0,
    var isSelected: Boolean,
    var topics: List<TopicJSON>?,
    var streamsId: Int?,
    var imageId: Int,
    override val viewType: Int,
    override var uid: String
) : ViewTyped

class AllStreamsViewHolder(val view: View, private val channelsInterface: UiHolderFactory.ChannelsInterface) :
    BaseViewHolder<TitleUi>(view) {
    private val title: TextView = view.findViewById(R.id.streamText)
    private val count: TextView = view.findViewById(R.id.messagesCount)
    private val image: ImageView = view.findViewById(R.id.streamImage)
    private val parentView: LinearLayout = view.findViewById(R.id.parentView)
    private val color1 = ResourcesCompat.getColor(view.resources, R.color.expand_item_color_two, null)
    private val color2 = ResourcesCompat.getColor(view.resources, R.color.expand_item_color_one, null)

    override fun bind(item: TitleUi) {
        view.setOnClickListener {
            channelsInterface.getClickedView(view, adapterPosition, item.viewType)
        }
        if (adapterPosition % 2 == 0 && item.viewType == R.layout.item_expand) {
            parentView.setBackgroundColor(color1)
        } else if (item.viewType == R.layout.item_expand) {
            parentView.setBackgroundColor(color2)
        }
        title.text = item.title
        count.text = item.count.toString()
        image.setImageResource(item.imageId)
    }
}

