package com.volboy.course_project.message_recycler_view

import android.view.View
import android.widget.TextView
import com.volboy.course_project.R

class TextUi(val title: String, val message: String, override val viewType: Int = R.layout.in_message_item) : ViewTyped

class MessageViewHolder(view: View, click: (View) -> Boolean) : BaseViewHolder<TextUi>(view) {
    private val title: TextView = view.findViewById(R.id.header)
    private val message: TextView = view.findViewById(R.id.message)

    init {
        view.setOnLongClickListener(click)
    }

    override fun bind(item: TextUi) {
        title.text = item.title
        message.text = item.message
    }
}