package com.volboy.courseproject.presentation.bottomfragment

import android.view.View
import com.volboy.courseproject.R
import com.volboy.courseproject.recyclerview.BaseViewHolder
import com.volboy.courseproject.recyclerview.HolderFactory

class EmojiHolderFactory(private val bottomEmojiInterface: BottomEmojiInterface) : HolderFactory() {
    override fun createViewHolder(view: View, viewType: Int): BaseViewHolder<*>? {
        return when (viewType) {
            R.layout.item_emoji_dialog -> EmojiViewHolder(view, bottomEmojiInterface)
            else -> null
        }
    }

    interface BottomEmojiInterface {
        fun getClickedView(view: View, position: Int)
    }
}