package com.volboy.course_project.emoji_recycler_view

import android.view.View
import android.widget.TextView
import com.volboy.course_project.R
import com.volboy.course_project.message_recycler_view.BaseViewHolder
import com.volboy.course_project.message_recycler_view.ViewTyped


class EmojiUi(val emoji: String, override val viewType: Int = R.layout.emoji_dialog_item) :
    ViewTyped

class EmojiViewHolder(view: View, private val click: (View) -> Unit) : BaseViewHolder<EmojiUi>(view) {
    private val emoji: TextView = view.findViewById(R.id.emoji)

    init {
        view.setOnClickListener(click)
    }

    override fun bind(item: EmojiUi) {
        emoji.text = item.emoji
    }
}