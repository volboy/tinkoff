package com.volboy.hw_2.message_recycler_view

import android.view.View
import android.widget.TextView
import com.volboy.hw_2.R


class EmojiUi(val emoji: String) : ViewTyped

class EmojiViewHolder(view: View, private val click: (View) -> Unit) : BaseViewHolder<EmojiUi>(view) {
    private val emoji: TextView = view.findViewById(R.id.emoji)

    init {
        view.setOnClickListener(click)
    }

    override fun bind(item: EmojiUi) {
        emoji.text = item.emoji
    }
}