package com.volboy.course_project.presentation.bottomfragment

import android.view.View
import com.volboy.course_project.R
import com.volboy.course_project.recyclerview.BaseViewHolder
import com.volboy.course_project.recyclerview.HolderFactory

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