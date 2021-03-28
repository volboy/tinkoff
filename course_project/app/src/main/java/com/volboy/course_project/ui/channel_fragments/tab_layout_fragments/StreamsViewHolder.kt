package com.volboy.course_project.ui.channel_fragments.tab_layout_fragments

import android.view.View
import com.volboy.course_project.R
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import com.volboy.course_project.message_recycler_view.BaseViewHolder
import com.volboy.course_project.message_recycler_view.ViewTyped

class TitleUi(
    val title: String,
    val count: String?,
    val topics: List<Pair<String, Int>>?,
    var imageId: Int,
    override val viewType: Int = R.layout.item_collapse,
    override var uid: String = ""
) : ViewTyped

class AllStreamsViewHolder(val view: View, private val channelsInterface: UiHolderFactory.ChannelsInterface) :
    BaseViewHolder<TitleUi>(view) {
    private val title: TextView = view.findViewById(R.id.streamText)
    private val count: TextView = view.findViewById(R.id.messagesCount)
    private val image: ImageView = view.findViewById(R.id.streamImage)
    private val parentView: LinearLayout = view.findViewById(R.id.parentView)
    private val color= ResourcesCompat.getColor(view.resources, R.color.expand_item_color_two, null)

    override fun bind(item: TitleUi) {
        view.setOnClickListener {
            channelsInterface.getClickedView(view, adapterPosition, item.viewType)
        }
        if (adapterPosition % 2 == 0 && item.viewType == R.layout.item_expand) {
            parentView.setBackgroundColor(color)
        }
        title.text = item.title
        count.text = item.count.toString()
        image.setImageResource(item.imageId)
    }
}

