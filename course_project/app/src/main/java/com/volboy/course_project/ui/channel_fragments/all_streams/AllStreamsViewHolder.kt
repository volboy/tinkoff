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

class AllStreamsViewHolder(view: View, click: (View) -> Unit) : BaseViewHolder<TitleUi>(view) {
    private val title: TextView = view.findViewById(R.id.streamText)
    private val count: TextView=view.findViewById(R.id.messagesCount)
    private val image: ImageView = view.findViewById(R.id.streamImage)

    init {
        view.setOnClickListener(click)
    }

    override fun bind(item: TitleUi) {
        title.text = item.title
        count.text=item.count.toString()
        item.imageId?.let { image.setImageResource(it) }
    }
}