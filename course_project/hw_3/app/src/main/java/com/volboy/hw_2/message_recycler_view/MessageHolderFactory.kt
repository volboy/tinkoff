package com.volboy.hw_2.message_recycler_view

import android.view.View
import com.volboy.hw_2.R

class MessageHolderFactory : HolderFactory() {
    override fun createViewHolder(view: View, viewType: Int): BaseViewHolder<*>? {
        return when (viewType) {
            R.layout.in_message_item -> MessageViewHolder(view)
            R.layout.out_message_item -> MessageViewHolder(view)
            R.layout.date_divider_item -> DataViewHolder(view)
            else -> null
        }
    }
}