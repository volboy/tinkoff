package com.volboy.course_project.message_recycler_view

import android.view.View
import android.widget.TextView
import com.volboy.course_project.R
import com.volboy.course_project.customviews.EmojiView
import com.volboy.course_project.customviews.FlexBoxLayout

class TextUi(
    val title: String,
    val message: String,
    override val viewType: Int = R.layout.in_message_item,
    override val uid: String = ""
) : ViewTyped

class MessageViewHolder(view: View, click: (View) -> Boolean) : BaseViewHolder<TextUi>(view) {
    private val title: TextView = view.findViewById(R.id.header)
    private val message: TextView = view.findViewById(R.id.message)
    private val flexBoxLayout: FlexBoxLayout = view.findViewById(R.id.flex_box_layout)
    private val emojiView: EmojiView = EmojiView(view.context)


    init {
        view.setOnLongClickListener(click)
    }

    override fun bind(item: TextUi) {
        title.text = item.title
        message.text = item.message
        emojiView.emoji=""
        emojiView.text="343434"
        //flexBoxLayout.addView(emojiView)

    }
}