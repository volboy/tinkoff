package com.volboy.course_project.message_recycler_view

import android.view.View
import com.volboy.course_project.R

class EmojjiHolderFactory(private val click: (View) -> Unit) : HolderFactory() {
    override fun createViewHolder(view: View, viewType: Int): BaseViewHolder<*>? {
        return when (viewType) {
            R.layout.emoji_dialog_item -> EmojiViewHolder(view, click)
            else -> null
        }
    }
}