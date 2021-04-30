package com.volboy.courseproject.presentation.bottomfragment

import android.view.View
import android.widget.TextView
import com.volboy.courseproject.R
import com.volboy.courseproject.databinding.ItemEmojiDialogBinding
import com.volboy.courseproject.recyclerview.BaseViewHolder
import com.volboy.courseproject.recyclerview.ViewTyped

class EmojiUi(
    val emoji: String,
    override val viewType: Int = R.layout.item_emoji_dialog
) : ViewTyped

class EmojiViewHolder(view: View, bottomEmojiListener: EmojiHolderFactory.BottomEmojiInterface) : BaseViewHolder<EmojiUi>(view) {
    private val emojiDialogItemBinding = ItemEmojiDialogBinding.bind(view)
    private val emoji: TextView = emojiDialogItemBinding.emoji

    init {
        view.setOnClickListener {
            bottomEmojiListener.getClickedView(it, adapterPosition)
        }
    }

    override fun bind(item: EmojiUi) {
        emoji.text = item.emoji
    }
}