package com.volboy.hw_2.message_recycler_view

import android.view.View
import com.volboy.hw_2.R

class EmojjiHolderFactory(private val click: (View) -> Unit) : HolderFactory() {
    override fun createViewHolder(view: View, viewType: Int): BaseViewHolder<*>? {
        return when (viewType) {
            R.layout.emoji_dialog_item -> EmojiViewHolder(view, click)
            else -> null
        }
    }
}