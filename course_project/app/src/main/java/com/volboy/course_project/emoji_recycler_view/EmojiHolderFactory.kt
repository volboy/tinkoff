package com.volboy.course_project.emoji_recycler_view

import android.view.View
import com.volboy.course_project.R
import com.volboy.course_project.message_recycler_view.BaseViewHolder
import com.volboy.course_project.message_recycler_view.HolderFactory

class EmojiHolderFactory(private val click: (View) -> Unit) : HolderFactory() {
    override fun createViewHolder(view: View, viewType: Int): BaseViewHolder<*>? {
        return when (viewType) {
            R.layout.item_emoji_dialog -> EmojiViewHolder(view, click)
            else -> null
        }
    }
}