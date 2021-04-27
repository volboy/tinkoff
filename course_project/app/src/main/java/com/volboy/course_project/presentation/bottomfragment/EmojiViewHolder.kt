package com.volboy.course_project.presentation.bottomfragment

import android.view.View
import android.widget.TextView
import com.volboy.course_project.R
import com.volboy.course_project.databinding.ItemEmojiDialogBinding
import com.volboy.course_project.message_recycler_view.BaseViewHolder
import com.volboy.course_project.message_recycler_view.ViewTyped

class EmojiUi(val emoji: String, override val viewType: Int = R.layout.item_emoji_dialog) : ViewTyped

class EmojiViewHolder(view: View, click: EmojiHolderFactory.BottomEmojiInterface) : BaseViewHolder<EmojiUi>(view) {
    private val emojiDialogItemBinding = ItemEmojiDialogBinding.bind(view)
    private val emoji: TextView = emojiDialogItemBinding.emoji

    init {
        view.setOnClickListener {
            click.getClickedView(it, adapterPosition)
        }
    }

    override fun bind(item: EmojiUi) {
        emoji.text = item.emoji
    }
}